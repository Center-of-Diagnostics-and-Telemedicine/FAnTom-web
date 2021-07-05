package repository

import kotlinx.coroutines.delay
import model.*
import debugLog
import model.fantom.FantomMarkTypeEntity
import model.fantom.FantomResearchInitModel
import remote.FantomLibraryDataSource
import java.awt.Color

class RemoteLibraryRepositoryImpl(
  private val remoteDataSource: FantomLibraryDataSource,
  override val libraryContainerId: String
) : RemoteLibraryRepository {

  override suspend fun initResearch(accessionNumber: String): FantomResearchInitModel {
    val response = try {
      remoteDataSource.initResearch(accessionNumber)
    } catch (e: Exception) {
      debugLog(e.localizedMessage)
      debugLog("maybe not initialized")
      delay(1000)
      debugLog("calling to initialize again")
      return initResearch(accessionNumber)
    }
    return when {
      response.response != null -> {
        debugLog("ResearchInitResponse income")
        val resultMarkTypes = mutableMapOf<String, FantomMarkTypeEntity>()
        response.dictionary?.first()?.map { maps ->
          maps.map { entry ->
            resultMarkTypes[entry.key] = transformMarkEntity(entry.value)
          }
        }
        return response.response!!.copy(dictionary = resultMarkTypes)
      }
      response.error != null -> {
        when (response.error!!.error) {
          ErrorStringCode.NOT_INITIALIZED_YET.value -> {
            debugLog("not initialized yet income")
            delay(1000)
            debugLog("calling to initialize again")
            initResearch(accessionNumber)
          }
          ErrorStringCode.RESEARCH_INITIALIZATION_FAILED.value -> {
            throw IllegalStateException("cant initialize research in library server")
          }
          else -> throw IllegalStateException("RemoteLibraryRepositoryImpl initResearch unrecognized errorCode ${response.error}")
        }
      }
      else -> throw IllegalStateException("RemoteLibraryRepositoryImpl initResearch unrecognized response")
    }
  }

  private fun transformMarkEntity(
    value: FantomMarkTypeEntity
  ): FantomMarkTypeEntity {
    val rgb = value.CLR?.replace("\\s".toRegex(), "")?.split(",")
    return if (rgb != null && rgb.size > 1) {
      val red = rgb[0]
      val green = rgb[1]
      val blue = rgb[2]
      if (red.isEmpty().not() && green.isEmpty().not() && blue.isEmpty().not()) {
        val color = Color(red.toInt(), green.toInt(), blue.toInt())
        val hex = "#" + Integer.toHexString(color.rgb).substring(2)
        value.copy(CLR = hex)
      } else value
    } else value
  }

  override suspend fun getSlice(request: SliceRequestNew, researchName: String): String {
    val response = remoteDataSource.getSlice(request, researchName)
    return when {
      response.response != null -> response.response!!.image
      response.error != null -> throw IllegalStateException("RemoteLibraryRepositoryImpl getSlice errorCode ${response.error?.error}")
      else -> throw IllegalStateException("RemoteLibraryRepositoryImpl getSlice unrecognized response")
    }
  }

  override suspend fun hounsfield(request: HounsfieldRequestNew): Double {
    val response = remoteDataSource.getHounsfield(request)
    return when {
      response.response != null -> response.response!!.brightness ?: 0.0
      response.error != null -> throw IllegalStateException("RemoteLibraryRepositoryImpl hounsfield errorCode ${response.error?.error}")
      else -> throw IllegalStateException("RemoteLibraryRepositoryImpl hounsfield unrecognized response")
    }
  }

  override suspend fun closeSession() {
    remoteDataSource.closeSession()
  }

}