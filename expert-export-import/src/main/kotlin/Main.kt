import model.UserRole
import model.hash
import model.macProtocolsPath
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import useCase.createResearch
import useCase.createRois
import useCase.createUserResearchesRelationModel
import useCase.getDoseReports

suspend fun main() {
  connectToDatabase()

  initDb()


//  import()
//  export()
  doseReports()
}

suspend fun doseReports() {
  val doseReports = getDoseReports(macProtocolsPath)

//  //создаем пользователей
//  userRepository.createUser(
//    login = "#tagger_01",
//    hashedPassword = hash("text_detector_v01"),
//    role = UserRole.TAGGER.value
//  )
//  userRepository.createUser(
//    login = "arbiter",
//    hashedPassword = hash("doseReportsArbiter"),
//    role = UserRole.ARBITER.value
//  )
//  val tagger = userRepository.getUser("#tagger_01", hash("text_detector_v01"))!!
  val expert = userRepository.getUser("arbiter", hash("doseReportsArbiter"))!!

  //создаем исследования
  val researches = doseReports.map { jsonFileModel ->
    createResearch(
      researchData = jsonFileModel.ids!!,
      researchRepository = researchRepository
    )
  }

  createUserResearchesRelationModel(
    usersModels = listOf(expert),
    researches = researches,
    repository = userResearchRepository
  )

  createRois(researches, doseReports, exportedRoisRepository)


//  val jsonFileModels = getFiles(macProtocolsPath)
//  jsonFileModels.forEach { jsonModel ->
//    val doctorIds = jsonModel.doctorComments.map { it.id }
//    val users = createDoctors(
//      doctorsIds = doctorIds,
//      userRepository = userRepository
//    )
//    val research = createResearch(
//      researchData = jsonModel.ids,
//      researchRepository = researchRepository
//    )
//    val userResearches = createUsersResearchRelationModel(
//      userModels = users,
//      researchModels = listOf(research),
//      repository = userResearchRepository
//    )
//    val userToNodule = mapNodules(jsonModel, users)
//
//    createMarks(
//      usersToNodules = userToNodule,
//      users = users,
//      research = research,
//      repository = exportedMarksRepository
//    )
//  }
}

//private suspend fun export() {
//  val researchId = 308
//  val userIds = userResearchRepository.getUsersForResearch(researchId).map { it.userId }
//  val marks = userIds.map { it to multiPlanarMarksRepository.getAll(it, researchId) }
//  val comments = marks.map { pair ->
//    val (userId, marks) = pair
//
//  }
//  val doctorsModels =
//
//
//  val researches = userIds.mapNotNull { researchRepository.getResearch(it.researchId) }
//  val researchMarksMap = researches.map { it to multiPlanarMarksRepository.getAll(userId, it.id) }
//  val comments = researchMarksMap.forEach { pair ->
//    val (researchModel, marks) = pair
//
//  }
//  val files = researches.map {
//    JsonFileModel(
//      doctorComments = listOf()
//    )
//  }
//
//}
//
//private suspend fun import() {
//  val jsonFileModels = getFiles(protocolsPath)
//  jsonFileModels.forEach { jsonModel ->
//    val doctorIds = jsonModel.doctorComments.map { it.id }
//    val users = createDoctors(
//      doctorsIds = doctorIds,
//      userRepository = userRepository
//    )
//    val research = createResearch(
//      researchData = jsonModel.ids,
//      researchRepository = researchRepository
//    )
//    val userResearches = createUsersResearchRelationModel(
//      userModels = users,
//      researchModels = listOf(research),
//      repository = userResearchRepository
//    )
//    val userToNodule = mapNodules(jsonModel, users)
//
//    createMarks(
//      usersToNodules = userToNodule,
//      users = users,
//      research = research,
//      repository = exportedMarksRepository
//    )
//  }
//}

private fun connectToDatabase() {
  Database.connect(
    url = "jdbc:mysql://localhost:3306/fantom_mg?characterEncoding=utf8&useUnicode=true&useSSL=false",
    driver = "com.mysql.jdbc.Driver",
    user = "root",
//      password = "vfrcbv16"
    password = ""
  )
}

fun initDb() {
  transaction {
    SchemaUtils.create(
      ExpertMarksVos,
      ExpertRoisVos
    )
    SchemaUtils.createMissingTablesAndColumns(
      ExpertMarksVos,
      ExpertRoisVos
    )
  }

}


