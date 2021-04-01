package repository

import model.ExpertRoiEntity

interface ExpertRoiRepository {

  val token: suspend () -> String

  suspend fun getRois(researchId: Int): List<ExpertRoiEntity>
  suspend fun saveRoi(markToSave: ExpertRoiEntity, researchId: Int): ExpertRoiEntity

}