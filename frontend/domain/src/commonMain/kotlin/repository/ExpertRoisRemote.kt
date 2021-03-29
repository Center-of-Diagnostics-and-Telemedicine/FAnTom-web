package repository

import model.ExpertRoiEntity
import model.ExpertRoisResponse

interface ExpertRoisRemote {
  suspend fun getRois(token: String, researchId: Int): ExpertRoisResponse
  suspend fun save(request: ExpertRoiEntity, researchId: Int, token: String): ExpertRoisResponse
}