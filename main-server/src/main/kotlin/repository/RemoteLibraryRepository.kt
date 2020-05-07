package repository

import fantom.FantomLibraryDataSource
import kotlinx.coroutines.delay
import model.*

interface RemoteLibraryRepository {
  val libraryContainerId: String
  suspend fun initResearch(accessionNumber: String): ApiResponse.ResearchInitResponse
  suspend fun getSlice(request: SliceRequest, researchName: String): ByteArray
  suspend fun hounsfield(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int)
}

class RemoteLibraryRepositoryImpl(
  private val remoteDataSource: FantomLibraryDataSource,
  override val libraryContainerId: String
) : RemoteLibraryRepository {

  override suspend fun initResearch(accessionNumber: String): ApiResponse.ResearchInitResponse {
    return when (val response = remoteDataSource.initResearch(accessionNumber)) {
      is ApiResponse.ResearchInitResponse -> {
        return response
      }
      is ApiResponse.ErrorResponse -> {
        when (response.error) {
          ErrorStringCode.NOT_INITIALIZED_YET.value -> {
            delay(1000)
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

  override suspend fun getSlice(request: SliceRequest, researchName: String): ByteArray {
    return when (val response = remoteDataSource.getSlice(request, researchName)) {
      is ApiResponse.SliceResponse -> response.image
      is ApiResponse.ErrorResponse -> throw IllegalStateException("RemoteLibraryRepositoryImpl getSlice errorCode ${response.error}")
      else -> throw IllegalStateException("RemoteLibraryRepositoryImpl getSlice unrecognized response")
    }
  }

  override suspend fun hounsfield(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int) {
    when (val response = remoteDataSource.getHounsfield(axialCoord, frontalCoord, sagittalCoord)) {
      is ApiResponse.HounsfieldResponse -> response.huValue
      is ApiResponse.ErrorResponse -> throw IllegalStateException("RemoteLibraryRepositoryImpl hounsfield errorCode ${response.error}")
      else -> throw IllegalStateException("RemoteLibraryRepositoryImpl hounsfield unrecognized response")
    }
  }

}