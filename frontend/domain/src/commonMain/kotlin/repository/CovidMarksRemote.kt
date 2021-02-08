package repository

import model.CovidMarkEntity
import model.CovidMarksResponse

interface CovidMarksRemote {
  suspend fun getMark(token: String, researchId: Int): CovidMarksResponse
  suspend fun save(request: CovidMarkEntity, researchId: Int, token: String): CovidMarksResponse
}