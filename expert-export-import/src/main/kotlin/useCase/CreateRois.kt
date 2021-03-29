package useCase

import model.*
import repository.repository.ExportedRoisRepository

suspend fun createRois(
  researches: List<ResearchModel>,
  doseReports: List<JsonFileModel>,
  repository: ExportedRoisRepository
): List<ExportedRoiModel> {
  val result = mutableListOf<ExportedRoiModel>()
  doseReports.forEach { fileModel ->
    fileModel.instances?.forEach { instanceModel ->
      instanceModel.rois.forEach { roiModel ->
        val researchId = researches.first {
          it.accessionNumber == fileModel.ids!!.accessionNumber
            && it.studyID == fileModel.ids!!.studyId
            && it.studyInstanceUID == fileModel.ids!!.studyInstanceUid
        }.id
        createRoi(
          repository = repository,
          instanceModel = instanceModel,
          roiModel = roiModel,
          researchId
        )?.let {
          result.add(it)
        }
      }
    }
  }
  return result
}

private suspend fun createRoi(
  repository: ExportedRoisRepository,
  instanceModel: InstanceModel,
  roiModel: RoiModel,
  researchId: Int
): ExportedRoiModel? {
  return repository.create(
    mark = ExportedRoiModel(
      id = -1,
      researchId = researchId,
      acquisitionNumber = instanceModel.acquisitionNumber,
      dcmFilename = instanceModel.dcmFilename,
      instanceNumber = instanceModel.instanceNumber,
      seriesNumber = instanceModel.seriesNumber,
      sopInstanceUid = instanceModel.sopInstanceUid,
      anatomicalLocation = roiModel.anatomicalLocation,
      confidence = roiModel.confidence,
      roiFilename = roiModel.roiFilename,
      roiShape = roiModel.roiShape,
      roiType = roiModel.roiType,
      roiTypeIndex = roiModel.roiTypeIndex,
      taggerId = roiModel.taggerId,
      xCenter = roiModel.xCenter,
      xSize = roiModel.xSize,
      yCenter = roiModel.yCenter,
      ySize = roiModel.ySize,
      text = roiModel.text,
    ),
    researchId = researchId
  )
}