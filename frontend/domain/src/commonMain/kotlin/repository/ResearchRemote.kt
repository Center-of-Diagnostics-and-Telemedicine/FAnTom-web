package repository

import model.*

interface ResearchRemote {
  suspend fun getAll(token: String): ResearchesResponse
  suspend fun init(token: String, id: Int): ResearchInitResponseNew
  suspend fun getSlice(
    token: String,
    request: SliceRequestNew,
    researchId: Int
  ): SliceResponse

  suspend fun hounsfield(
    token: String,
    request: HounsfieldRequest
  ): HounsfieldResponse

  suspend fun confirmCtTypeForResearch(
    token: String,
    request: ConfirmCTTypeRequest
  ): BaseResponse

  suspend fun closeSession(token: String, researchId: Int): BaseResponse
}