package dao

import ExpertMarksVos
import model.ExportedMarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.dao.ExportedMarksDaoFacade
import toExportedMarkModel

class ExportedMarksDao : ExportedMarksDaoFacade {
  override suspend fun get(id: Int): ExportedMarkModel? {
    return transaction {
      ExpertMarksVos
        .select { ExpertMarksVos.id eq id }
        .firstOrNull()
        ?.toExportedMarkModel()
    }
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<ExportedMarkModel> {
    return transaction {
      ExpertMarksVos
        .select { ExpertMarksVos.researchId.eq(researchId).and(ExpertMarksVos.userId.eq(userId)) }
        .map(ResultRow::toExportedMarkModel)
    }
  }

  override suspend fun save(mark: ExportedMarkModel, userrId: Int, researchhId: Int): Int {
    return transaction {
      ExpertMarksVos.insert {
        it[userId] = userrId
        it[researchId] = researchhId
        it[diameterMm] = mark.diameterMm
        it[type] = mark.type
        it[version] = mark.version
        it[x] = mark.x
        it[y] = mark.y
        it[z] = mark.z
        it[zType] = mark.zType
        it[expertDecisionMachineLearning] = if (mark.expertDecisionMachineLearning == true) 1 else 0
        it[expertDecisionProperSize] = if (mark.expertDecisionProperSize == true) 1 else 0
        it[expertDecision] = mark.expertDecision
        it[expertDecisionId] = mark.expertDecisionId
        it[expertDecisionComment] = mark.expertDecisionComment
        it[expertDecisionType] = mark.expertDecisionType
      } get ExpertMarksVos.id
    }
  }

  override suspend fun update(mark: ExportedMarkModel) {
    return transaction {
      ExpertMarksVos.update(where = { ExpertMarksVos.id eq mark.id }) {
        it[diameterMm] = mark.diameterMm
        it[type] = mark.type
        it[version] = mark.version
        it[x] = mark.x
        it[y] = mark.y
        it[z] = mark.z
        it[zType] = mark.zType
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