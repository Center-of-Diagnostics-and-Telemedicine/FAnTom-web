package repository

import model.*
import model.init.ResearchInitResponse

interface ResearchRemote {
  suspend fun getAll(token: String): ResearchesResponse
  suspend fun init(token: String, id: Int): ResearchInitResponse
  suspend fun getSlice(
    token: String,
    request: SliceRequestNew,
    researchId: Int
  ): SliceResponse

  suspend fun hounsfield(
    token: String,
    request: HounsfieldRequestNew
  ): HounsfieldResponse

  suspend fun confirmCtTypeForResearch(
    token: String,
    request: ConfirmCTTypeRequest
  ): BaseResponse

  suspend fun closeSession(token: String, researchId: Int): BaseResponse
  suspend fun closeResearch(token: String, researchId: Int): BaseResponse
}