package client.datasource.remote

import model.*

interface ResearchRemote {
  suspend fun getResearches(token: String): ResearchesResponse
  suspend fun initResearch(token: String, researchId: Int): ResearchInitResponse
  suspend fun getSlice(
    token: String,
    request: SliceRequest,
    researchId: Int
  ): SliceResponse

  suspend fun getHounsfieldData(
    token: String,
    request: HounsfieldRequest
  ): HounsfieldResponse

  suspend fun confirmCtTypeForResearch(
    token: String,
    request: ConfirmCTTypeRequest
  ): BaseResponse

  suspend fun closeSession(token: String): BaseResponse
}