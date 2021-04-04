package useCase

import model.*
import repository.repository.ExpertMarksRepository
import repository.repository.ExportedRoisRepository
import repository.repository.UserExpertMarkRepository

suspend fun createRois(
  researches: List<ResearchModel>,
  doseReports: List<JsonFileModel>,
  repository: ExportedRoisRepository,
  expertMarksRepository: ExpertMarksRepository,
  userExpertMarkRepository: UserExpertMarkRepository,
  taggerId: Int,
  arbiterIds: List<Int>
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
        )?.let { roi ->
          result.add(roi)
          createMark(
            repository = expertMarksRepository,
            roiModel = roi,
            researchId = researchId,
            taggerId = taggerId
          )?.let { mark ->
            arbiterIds.forEach { arbiterId ->
              createUserExpertMark(
                repository = userExpertMarkRepository,
                expertMarkId = mark.id,
                userId = arbiterId,
                researchId = researchId
              )
            }
          }
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

private suspend fun createMark(
  repository: ExpertMarksRepository,
  roiModel: ExportedRoiModel,
  researchId: Int,
  taggerId: Int
): ExpertMarkModel? {
  return repository.create(
    mark = ExpertMarkModel(
      id = -1,
      roiId = roiModel.id,
      xCenter = roiModel.xCenter,
      yCenter = roiModel.yCenter,
      xSize = roiModel.xSize,
      ySize = roiModel.ySize,
      researchId = researchId,
      acquisitionNumber = roiModel.acquisitionNumber,
      dcmFilename = roiModel.dcmFilename,
      instanceNumber = roiModel.instanceNumber,
      seriesNumber = roiModel.seriesNumber,
      sopInstanceUid = roiModel.sopInstanceUid,
      anatomicalLocation = roiModel.anatomicalLocation,
      confidence = roiModel.confidence,
      roiFilename = roiModel.roiFilename,
      roiShape = roiModel.roiShape,
      roiType = roiModel.roiType,
      roiTypeIndex = roiModel.roiTypeIndex,
      taggerId = roiModel.taggerId,
      text = roiModel.text,
      confirmed = null,
    ),
    userId = taggerId,
    researchId = researchId
  )
}

private suspend fun createUserExpertMark(
  repository: UserExpertMarkRepository,
  expertMarkId: Int,
  userId: Int,
  researchId: Int
) {
  repository.createUserExpertMark(
    UserExpertMarkModel(
      userId = userId,
      researchId = researchId,
      markId = expertMarkId
    )
  )
}