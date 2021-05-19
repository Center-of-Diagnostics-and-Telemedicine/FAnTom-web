package dao

import ExpertRoisVos
import model.ExportedRoiModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toExportedInstanceModel

class ExportedRoisDao : ExportedRoisDaoFacade {
  override suspend fun get(id: Int): ExportedRoiModel? {
    return transaction {
      ExpertRoisVos
        .select { ExpertRoisVos.id eq id }
        .firstOrNull()
        ?.toExportedInstanceModel()
    }
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<ExportedRoiModel> {
    return transaction {
      ExpertRoisVos
        .select {
          ExpertRoisVos.researchId.eq(researchId)
        }
        .map(ResultRow::toExportedInstanceModel)
    }
  }

  override suspend fun save(mark: ExportedRoiModel, researchhId: Int): Int {
    return transaction {
      ExpertRoisVos.insert {
        it[researchId] = researchhId
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
        it[xCenter] = mark.xCenter
        it[xSize] = mark.xSize
        it[yCenter] = mark.yCenter
        it[ySize] = mark.ySize
        it[text] = mark.text
      } get ExpertRoisVos.id
    }
  }

  override suspend fun update(mark: ExportedRoiModel) {
    return transaction {
      ExpertRoisVos.update(where = { ExpertRoisVos.id eq mark.id }) {
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
        it[xCenter] = mark.xCenter
        it[xSize] = mark.xSize
        it[yCenter] = mark.yCenter
        it[ySize] = mark.ySize
        it[text] = mark.text
      }
    }
  }

  override suspend fun delete(id: Int) {
    return transaction {
      ExpertRoisVos.deleteWhere { ExpertRoisVos.id.eq(id) }
    }
  }

}