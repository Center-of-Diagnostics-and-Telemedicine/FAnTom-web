package repository

import fantom.FantomLibraryDataSource
import kotlinx.coroutines.delay
import model.*
import util.debugLog

interface RemoteLibraryRepository {
  val libraryContainerId: String
  suspend fun initResearch(accessionNumber: String): ResearchInitModelNew
  suspend fun getSlice(request: SliceRequestNew, researchName: String): String
  suspend fun hounsfield(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): Double
}

class RemoteLibraryRepositoryImpl(
  private val remoteDataSource: FantomLibraryDataSource,
  override val libraryContainerId: String
) : RemoteLibraryRepository {

  override suspend fun initResearch(accessionNumber: String): ResearchInitModelNew {
    val response = try {
      remoteDataSource.initResearch(accessionNumber)
    } catch (e: Exception) {
      debugLog("maybe not initialized")
      delay(1000)
      debugLog("calling to initialize again")
      return initResearch(accessionNumber)
    }

    debugLog(response.toString())
    return when {
      response.response != null -> {
        debugLog("ResearchInitResponse income")
        return response.response!!
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

  override suspend fun hounsfield(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): Double {
    val response = remoteDataSource.getHounsfield(axialCoord, frontalCoord, sagittalCoord)
    return when {
      response.response != null -> response.response!!.huValue
      response.error != null -> throw IllegalStateException("RemoteLibraryRepositoryImpl hounsfield errorCode ${response.error?.error}")
      else -> throw IllegalStateException("RemoteLibraryRepositoryImpl hounsfield unrecognized response")
    }
  }

}