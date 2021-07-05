package repository

import debugLog
import kotlinx.coroutines.delay
import model.ErrorStringCode
import model.HounsfieldRequestNew
import model.init.ResearchInitModel
import model.SliceRequestNew
import remote.FantomLibraryDataSource
import remote.mappers.toResponse

class RemoteLibraryRepositoryImpl(
  private val remoteDataSource: FantomLibraryDataSource,
  override val libraryContainerId: String
) : RemoteLibraryRepository {

  override suspend fun initResearch(accessionNumber: String): ResearchInitModel {
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
      response.response != null -> response.response!!.toResponse(response.dictionary)
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
