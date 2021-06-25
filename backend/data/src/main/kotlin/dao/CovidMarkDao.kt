package dao

import model.CovidMarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.dao.CovidMarkDaoFacade
import toCovidMark

class CovidMarkDao : CovidMarkDaoFacade {
  override suspend fun getMark(userId: Int, researchId: Int): CovidMarkModel? {
    return transaction {
      CovidMarksVos.select { CovidMarksVos.researchId eq researchId and (CovidMarksVos.userId eq userId) }
        .firstOrNull()
        ?.toCovidMark()
    }
  }

  override suspend fun createMark(covidMark: CovidMarkModel) {
    return transaction {
      CovidMarksVos.insert {
        it[userId] = covidMark.userId
        it[researchId] = covidMark.researchId
        it[rightUpperLobeValue] = covidMark.rightUpperLobeValue
        it[middleLobeValue] = covidMark.middleLobeValue
        it[rightLowerLobeValue] = covidMark.rightLowerLobeValue
        it[leftUpperLobeValue] = covidMark.leftUpperLobeValue
        it[leftLowerLobeValue] = covidMark.leftLowerLobeValue
      }
    }
  }

  override suspend fun deleteMark(researchId: Int, userId: Int) {
    transaction {
      CovidMarksVos.deleteWhere { CovidMarksVos.researchId eq researchId and (CovidMarksVos.userId eq userId) }
    }
  }

  override suspend fun updateMark(covidMark: CovidMarkModel) {
    return transaction {
      CovidMarksVos.update(where = { CovidMarksVos.researchId eq covidMark.researchId and (CovidMarksVos.userId eq covidMark.userId) }) {
        it[userId] = covidMark.userId
        it[researchId] = covidMark.researchId
        it[rightUpperLobeValue] = covidMark.rightUpperLobeValue
        it[middleLobeValue] = covidMark.middleLobeValue
        it[rightLowerLobeValue] = covidMark.rightLowerLobeValue
        it[leftUpperLobeValue] = covidMark.leftUpperLobeValue
        it[leftLowerLobeValue] = covidMark.leftLowerLobeValue
      }
    }
  }


}