package dao

import CovidMarksVos
import model.CovidMark
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toCovidMark

interface CovidMarkDaoFacade {
  suspend fun getMark(userId: Int, researchId: Int): CovidMark?
  suspend fun createMark(covidMark: CovidMark)
  suspend fun deleteMark(researchId: Int, userId: Int)
  suspend fun updateMark(covidMark: CovidMark)
}

class CovidCovidMarkDao : CovidMarkDaoFacade {
  override suspend fun getMark(userId: Int, researchId: Int): CovidMark? {
    return transaction {
      CovidMarksVos.select { CovidMarksVos.researchId eq researchId and (CovidMarksVos.userId eq userId) }
      .firstOrNull()
      ?.toCovidMark()
    }
  }

  override suspend fun createMark(covidMark: CovidMark) {
    return transaction {
      CovidMarksVos.insert {
        it[userId] = covidMark.userId
        it[researchId] = covidMark.researchId
        it[ctType] = covidMark.ctType
        it[leftPercent] = covidMark.leftPercent
        it[rightPercent] = covidMark.rightPercent
      }
    }
  }

  override suspend fun deleteMark(researchId: Int, userId: Int) {
    transaction {
      CovidMarksVos.deleteWhere { CovidMarksVos.researchId eq researchId and (CovidMarksVos.userId eq userId) }
    }
  }

  override suspend fun updateMark(covidMark: CovidMark) {
    return transaction {
      CovidMarksVos.update(where = { CovidMarksVos.researchId eq covidMark.researchId and (CovidMarksVos.userId eq covidMark.userId) }) {
        it[userId] = covidMark.userId
        it[researchId] = covidMark.researchId
        it[ctType] = covidMark.ctType
        it[leftPercent] = covidMark.leftPercent
        it[rightPercent] = covidMark.rightPercent
      }
    }
  }


}
