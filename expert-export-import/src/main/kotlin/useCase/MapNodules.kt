package useCase

import model.DEFAULT_USER_NAME
import model.JsonFileModel
import model.NoduleModel
import model.UserModel

fun mapNodules(
  jsonFileModel: JsonFileModel,
  users: List<UserModel>
): List<Map<UserModel, NoduleModel?>> {
  val nodules = mutableListOf<Map<UserModel, NoduleModel?>>()

  jsonFileModel.nodules?.firstOrNull()?.let { nodulesParent ->
    val mapToAdd = mutableMapOf<UserModel, NoduleModel?>()
    nodulesParent.firstOrNull()?.let { map ->
      map.forEach { doctorId, noduleModel ->
        val userLogin = DEFAULT_USER_NAME + "_" + doctorId
        val user = users.first { it.name == userLogin }
        mapToAdd[user] = noduleModel
      }
    }
    nodules.add(mapToAdd)
  }

  return nodules
}