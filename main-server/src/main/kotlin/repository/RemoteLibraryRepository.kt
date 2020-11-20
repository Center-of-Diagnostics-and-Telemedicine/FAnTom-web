package repository

import fantom.FantomLibraryDataSource
import io.ktor.util.*
import kotlinx.coroutines.delay
import model.*
import util.debugLog
import java.awt.Color

interface RemoteLibraryRepository {
  val libraryContainerId: String
  suspend fun initResearch(accessionNumber: String): ResearchInitModelNew
  suspend fun getSlice(request: SliceRequestNew, researchName: String): String
  suspend fun hounsfield(request: HounsfieldRequestNew): Double
  suspend fun closeSession()
}

class RemoteLibraryRepositoryImpl(
  private val remoteDataSource: FantomLibraryDataSource,
  override val libraryContainerId: String
) : RemoteLibraryRepository {

  override suspend fun initResearch(accessionNumber: String): ResearchInitModelNew {
    val response = try {
      remoteDataSource.initResearch(accessionNumber)
    } catch (e: Exception) {
      debugLog(e.localizedMessage)
      debugLog("maybe not initialized")
      delay(1000)
      debugLog("calling to initialize again")
      return initResearch(accessionNumber)
    }

//    debugLog(response.toString())
    return when {
      response.response != null -> {
        debugLog("ResearchInitResponse income")
        val resultMarkTypes = mutableMapOf<String, MarkTypeEntity>()
        response.dictionary?.first()?.map { maps ->
          maps.map { entry ->
            val rgb = entry.value.CLR?.replace("\\s".toRegex(), "")?.split(",")

            val red = rgb?.get(0)
            val green = rgb?.get(1)
            val blue = rgb?.get(2)
            val markTypeEntity = if (red.isNullOrEmpty().not() && green.isNullOrEmpty().not() && blue.isNullOrEmpty().not()) {
              val color = Color(red!!.toInt(),green!!.toInt(),blue!!.toInt())
              val hex = "#" + Integer.toHexString(color.getRGB()).substring(2)
//              val hexColor = String.format("#%02x%02x%02x", red, green, blue)
              entry.value.copy(CLR = hex)
            } else entry.value
            resultMarkTypes[entry.key] = markTypeEntity

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