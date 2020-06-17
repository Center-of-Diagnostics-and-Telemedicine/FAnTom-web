package dao

import CovidMarksVos
import model.MarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toCovidMark

interface CovidMarkDaoFacade {
  suspend fun getMark(userId: Int, researchId: Int): MarkModel?
  suspend fun createMark(markModel: MarkModel)
  suspend fun deleteMark(researchId: Int, userId: Int)
  suspend fun updateMark(markModel: MarkModel)
}

class CovidCovidMarkDao : CovidMarkDaoFacade {
  override suspend fun getMark(userId: Int, researchId: Int): MarkModel? {
    return transaction {
      CovidMarksVos.select { CovidMarksVos.researchId eq researchId and (CovidMarksVos.userId eq userId) }
      .firstOrNull()
      ?.toCovidMark()
    }
  }

  override suspend fun createMark(markModel: MarkModel) {
    return transaction {
      CovidMarksVos.insert {
        it[userId] = markModel.userId
        it[researchId] = markModel.researchId
        it[ctType] = markModel.ctType
        it[leftPercent] = markModel.leftPercent
        it[rightPercent] = markModel.rightPercent
      }
    }
  }

  override suspend fun deleteMark(researchId: Int, userId: Int) {
    transaction {
      CovidMarksVos.deleteWhere { CovidMarksVos.researchId eq researchId and (CovidMarksVos.userId eq userId) }
    }
  }

  override suspend fun updateMark(markModel: MarkModel) {
    return transaction {
      CovidMarksVos.update(where = { CovidMarksVos.researchId eq markModel.researchId and (CovidMarksVos.userId eq markModel.userId) }) {
        it[userId] = markModel.userId
        it[researchId] = markModel.researchId
        it[ctType] = markModel.ctType
        it[leftPercent] = markModel.leftPercent
        it[rightPercent] = markModel.rightPercent
      }
    }
  }


}
