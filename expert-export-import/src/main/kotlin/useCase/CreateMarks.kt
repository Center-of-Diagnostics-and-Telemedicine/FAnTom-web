package useCase

import NoduleModel
import model.MarkData
import model.MarkEntity
import model.ResearchModel
import model.UserModel
import repository.MarksRepository

suspend fun createMarks(
  usersToNodules: List<Map<UserModel, NoduleModel?>>,
  users: List<UserModel>,
  researches: List<ResearchModel>,
  repository: MarksRepository
): MarkEntity {
  usersToNodules.forEach { usersToNodulesMap ->
    usersToNodulesMap.forEach { user, nodule ->
      if (nodule != null) {
        repository.create(
          MarkData(
            x = nodule.x,
            y = nodule.y,
            z = nodule.z,
            radiusHorizontal = nodule.diameterMm,
            radiusVertical = nodule.diameterMm,
            sizeVertical = ,
            sizeHorizontal =,
            cutType =,
            shapeType =,
          )
        )
      }
    }
  }
}