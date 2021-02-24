package dao

import CovidMarksVos
import model.CovidMarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toCovidMark

interface CovidMarkDaoFacade {
  suspend fun getMark(userId: Int, researchId: Int): CovidMarkModel?
  suspend fun createMark(covidMark: CovidMarkModel)
  suspend fun deleteMark(researchId: Int, userId: Int)
  suspend fun updateMark(covidMark: CovidMarkModel)
}
