package repository

import model.BaseResponse
import model.ExpertMarkEntity
import model.ExpertMarksResponse

interface ExpertMarksRemote {
  suspend fun getMark(token: String, researchId: Int): ExpertMarksResponse
  suspend fun save(request: ExpertMarkEntity, researchId: Int, token: String): ExpertMarksResponse
  suspend fun update(request: ExpertMarkEntity, researchId: Int, token: String): BaseResponse
}