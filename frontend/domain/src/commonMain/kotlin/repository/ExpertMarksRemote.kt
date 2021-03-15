package repository

import model.CovidMarkEntity
import model.ExpertMarkEntity
import model.ExpertMarksResponse

interface ExpertMarksRemote {
  suspend fun getMark(token: String, researchId: Int): ExpertMarksResponse
  suspend fun save(request: ExpertMarkEntity, researchId: Int, token: String): ExpertMarksResponse
}