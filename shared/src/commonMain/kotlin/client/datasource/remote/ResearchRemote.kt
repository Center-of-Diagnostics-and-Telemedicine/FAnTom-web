package client.datasource.remote

import model.*

interface ResearchRemote {
  suspend fun getResearches(token: String): ApiResponse
  suspend fun initResearch(token: String, researchId: Int): ApiResponse
  suspend fun getSlice(token: String, request: SliceRequest, researchId: Int): ApiResponse
  suspend fun getHounsfieldData(
    token: String,
    request: HounsfieldRequest
  ): ApiResponse

  suspend fun confirmCtTypeForResearch(token: String, request: ConfirmCTTypeRequest): ApiResponse
  suspend fun closeSession(token: String): ApiResponse
}