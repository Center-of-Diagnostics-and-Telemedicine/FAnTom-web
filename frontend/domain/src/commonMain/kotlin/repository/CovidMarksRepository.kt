package repository

import model.CovidMarkEntity

interface CovidMarksRepository {

  val token: suspend () -> String

  suspend fun getMark(researchId: Int): CovidMarkEntity
  suspend fun saveMark(markToSave: CovidMarkEntity, researchId: Int)

}