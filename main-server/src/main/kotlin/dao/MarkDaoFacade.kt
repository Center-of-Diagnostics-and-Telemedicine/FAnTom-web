package dao

import MarkVos
import model.MarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toMark

interface MarkDaoFacade {
  suspend fun getMark(userId: Int, researchId: Int): MarkModel?
  suspend fun createMark(markModel: MarkModel)
  suspend fun deleteMark(researchId: Int, userId: Int)
  suspend fun updateMark(markModel: MarkModel)
}

class MarkDao() : MarkDaoFacade {
  override suspend fun getMark(userId: Int, researchId: Int): MarkModel? {
    return transaction {
      MarkVos.select { MarkVos.researchId eq researchId and (MarkVos.userId eq userId) }
      .firstOrNull()
      ?.toMark()
    }
  }

  override suspend fun createMark(markModel: MarkModel) {
    return transaction {
      MarkVos.insert {
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
      MarkVos.deleteWhere { MarkVos.researchId eq researchId and (MarkVos.userId eq userId) }
    }
  }

  override suspend fun updateMark(markModel: MarkModel) {
    return transaction {
      MarkVos.update(where = { MarkVos.researchId eq markModel.researchId and (MarkVos.userId eq markModel.userId) }) {
        it[userId] = markModel.userId
        it[researchId] = markModel.researchId
        it[ctType] = markModel.ctType
        it[leftPercent] = markModel.leftPercent
        it[rightPercent] = markModel.rightPercent
      }
    }
  }


}