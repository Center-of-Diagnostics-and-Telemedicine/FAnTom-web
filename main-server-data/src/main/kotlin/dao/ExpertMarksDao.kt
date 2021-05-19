package dao

import ExpertMarksVos
import model.ExpertMarkModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
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

  override suspend fun getAll(researchId: Int): List<ExpertMarkModel> {
    return transaction {
      ExpertMarksVos
        .select { ExpertMarksVos.researchId.eq(researchId) }
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
        it[acquisitionNumber] = mark.acquisitionNumber
        it[dcmFilename] = mark.dcmFilename
        it[instanceNumber] = mark.instanceNumber
        it[seriesNumber] = mark.seriesNumber
        it[sopInstanceUid] = mark.sopInstanceUid
        it[anatomicalLocation] = mark.anatomicalLocation
        it[confidence] = mark.confidence
        it[roiFilename] = mark.roiFilename
        it[roiShape] = mark.roiShape
        it[roiType] = mark.roiType
        it[roiTypeIndex] = mark.roiTypeIndex
        it[taggerId] = mark.taggerId
        it[text] = mark.text
        it[confirmed] = mark.confirmed
      } get ExpertMarksVos.id
    }
  }

  override suspend fun update(mark: ExpertMarkModel) {
    return transaction {
      ExpertMarksVos.update(where = { ExpertMarksVos.id eq mark.id }) {
        it[xCenter] = mark.xCenter
        it[yCenter] = mark.yCenter
        it[xSize] = mark.xSize
        it[ySize] = mark.ySize
        it[acquisitionNumber] = mark.acquisitionNumber
        it[dcmFilename] = mark.dcmFilename
        it[instanceNumber] = mark.instanceNumber
        it[seriesNumber] = mark.seriesNumber
        it[sopInstanceUid] = mark.sopInstanceUid
        it[anatomicalLocation] = mark.anatomicalLocation
        it[confidence] = mark.confidence
        it[roiFilename] = mark.roiFilename
        it[roiShape] = mark.roiShape
        it[roiType] = mark.roiType
        it[roiTypeIndex] = mark.roiTypeIndex
        it[taggerId] = mark.taggerId
        it[text] = mark.text
        it[confirmed] = mark.confirmed
      }
    }
  }

  override suspend fun delete(id: Int) {
    return transaction {
      ExpertMarksVos.deleteWhere { ExpertMarksVos.id.eq(id) }
    }
  }

}