package dao

import ExpertMarksVos
import model.ExpertMarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.dao.ExpertMarksDaoFacade
import toExportedMarkModel

class ExpertMarksDao : ExpertMarksDaoFacade {
  override suspend fun get(id: Int): ExpertMarkModel? {
    return transaction {
      ExpertMarksVos
        .select { ExpertMarksVos.id eq id }
        .firstOrNull()
        ?.toExportedMarkModel()
    }
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<ExpertMarkModel> {
    return transaction {
      ExpertMarksVos
        .select { ExpertMarksVos.researchId.eq(researchId).and(ExpertMarksVos.userId.eq(userId)) }
        .map(ResultRow::toExportedMarkModel)
    }
  }

  override suspend fun save(
    mark: ExpertMarkModel,
    userrId: Int,
    researchhId: Int,
  ): Int {
    return transaction {
      ExpertMarksVos.insert {
        it[userId] = userrId
        it[researchId] = researchhId
        it[roiId] = mark.roiId
        it[xCenter] = mark.xCenter
        it[yCenter] = mark.yCenter
        it[xSize] = mark.xSize
        it[ySize] = mark.ySize
        it[expertDecisionMachineLearning] = if (mark.expertDecisionMachineLearning == true) 1 else 0
        it[expertDecisionProperSize] = if (mark.expertDecisionProperSize == true) 1 else 0
        it[expertDecision] = mark.expertDecision
        it[expertDecisionId] = mark.expertDecisionId
        it[expertDecisionComment] = mark.expertDecisionComment
        it[expertDecisionType] = mark.expertDecisionType
      } get ExpertMarksVos.id
    }
  }

  override suspend fun update(mark: ExpertMarkModel) {
    return transaction {
      ExpertMarksVos.update(where = { ExpertMarksVos.id eq mark.id }) {
        it[roiId] = mark.roiId
        it[xCenter] = mark.xCenter
        it[yCenter] = mark.yCenter
        it[xSize] = mark.xSize
        it[ySize] = mark.ySize
        it[expertDecisionMachineLearning] = if (mark.expertDecisionMachineLearning == true) 1 else 0
        it[expertDecisionProperSize] = if (mark.expertDecisionProperSize == true) 1 else 0
        it[expertDecision] = mark.expertDecision
        it[expertDecisionId] = mark.expertDecisionId
        it[expertDecisionComment] = mark.expertDecisionComment
        it[expertDecisionType] = mark.expertDecisionType
      }
    }
  }

  override suspend fun delete(id: Int) {
    return transaction {
      ExpertMarksVos.deleteWhere { ExpertMarksVos.id.eq(id) }
    }
  }

}