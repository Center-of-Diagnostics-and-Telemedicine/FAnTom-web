package useCase

import exportedMarksRepository
import model.protocolsPath
import researchRepository
import userRepository
import userResearchRepository

suspend fun importPlanar() {
  val jsonFileModels = getFiles(protocolsPath)
  jsonFileModels
    .filter { it.ids != null }
    .filter { it.taggers != null }
    .filter { it.instances != null }
    .map { jsonFileModel -> //каждый jsonFileModel является исследованием с отметками и разметчиками

      //создаем исследование в бд
      val research = createResearch(jsonFileModel.ids!!, researchRepository)

      //создаем разметчиков в бд
      val taggers = createTaggers(jsonFileModel.taggers!!, userRepository)

      //связываем разметчиков и исследование
      val usersResearch =
        createUsersResearchRelationModel(taggers, research, userResearchRepository)

      //создаем отметки
      val marks = jsonFileModel.instances!!


//      val doctorIds = jsonModel.doctorComments.map { it.id }
//      val users = createDoctors(
//        doctorsIds = doctorIds,
//        userRepository = userRepository
//      )
//
//      val userResearches = createUsersResearchRelationModel(
//        userModels = users,
//        researchModels = listOf(research),
//        repository = userResearchRepository
//      )
//      val userToNodule = mapNodules(jsonModel, users)
//
//      createMarks(
//        usersToNodules = userToNodule,
//        users = users,
//        research = research,
//        repository = exportedMarksRepository
//      )
    }
}