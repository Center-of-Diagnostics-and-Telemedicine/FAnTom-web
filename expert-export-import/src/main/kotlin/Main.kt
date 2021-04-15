import model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import useCase.getDoseReports
import useCase.saveDoseReports

suspend fun main() {
  connectToDatabase()

  initDb()


//  import()
//  export()
//  importDoseReports()
  exportDoseReports()
}

suspend fun exportDoseReports() {
  val users = userRepository.getAll()
  val researches =
    researchRepository.getAll().filter { it.category == DOSE_REPORT_RESEARCH_CATEGORY }
  val usersResearches = userResearchRepository.getAll()
  val doseReports = getDoseReports(protocolsPath)

  val resultToSave = doseReports.map { jsonFileModel ->

    val research = researches.first {
      val accessionNumber = it.accessionNumber == jsonFileModel.ids!!.accessionNumber
      val studyUid = it.studyID == jsonFileModel.ids!!.studyId
      val studyInstanceUid = it.studyInstanceUID == jsonFileModel.ids!!.studyInstanceUid
      accessionNumber && studyUid && studyInstanceUid
    }

    val taggersIds = usersResearches.filter { it.researchId == research.id }.map { it.userId }
    val taggersUsers =
      users
        .filter { user -> taggersIds.firstOrNull { it == user.id } != null }
        .associate {
          it.name to TaggerModel("fantom", it.id.toString())
        }
    val allRois = expertMarksRepository.getAll(research.id).groupBy { it.dcmFilename }
    val instances = allRois.map { entry: Map.Entry<String, List<ExpertMarkModel>> ->
      val firstRoi = entry.value.first()
      InstanceModel(
        acquisitionNumber = firstRoi.acquisitionNumber,
        dcmFilename = firstRoi.dcmFilename,
        instanceNumber = firstRoi.instanceNumber,
        seriesNumber = firstRoi.seriesNumber,
        sopInstanceUid = firstRoi.sopInstanceUid,
        rois = entry.value
          .filter { it.confirmed == true }
          .map { mark ->
            val taggerName = users.first { it.id == mark.userId }.name
            mark.toRoiModel(taggerName)
          },
      )
    }


    jsonFileModel.copy(
      taggers = jsonFileModel.taggers!!.plus(taggersUsers),
      instances = instances
    )
  }

  saveDoseReports("/${protocolsPath}/exported", resultToSave)
}

//suspend fun importDoseReports() {
//  val doseReports = getDoseReports(protocolsPath)
//
//  //создаем пользователей
//  userRepository.createUser(
//    login = "#tagger_01",
//    hashedPassword = hash("text_detector_v01"),
//    role = UserRole.TAGGER.value
//  )
//  val tagger = userRepository.getUser("#tagger_01", hash("text_detector_v01"))!!
//
//  val arbiterLogin = "arbiter"
//  val passwords = listOf(
//    "8HfYY3WDh4", //arbiter0 // Максим
//    "SQLXDey6EM", //arbiter1 // Николай Сергеевич
//    "ZQrR5srg8B", //arbiter2 // Владимир
//    "fjCBx2NWnR", //arbiter3 // Роман
//    "jeWx7Frqeq", //arbiter4 // Сергей
//    "MKddf3BNy2", //arbiter5 // Мария
//    "9G282uVZuc", //arbiter6 // Денис
//    "Ns3Cwx7g5r", //arbiter7 // Владимир?
//    "276dVrkets", //arbiter8 // Владимир?
//    "mnHV6sZXMt", //arbiter9 // Максим?
//  )
//  val experts = mutableListOf<UserModel>()
//  for (i in 0..9) {
//    val login = "$arbiterLogin$i"
//    val hashedPassword = hash(passwords[i])
//    userRepository.createUser(
//      login = login,
//      hashedPassword = hashedPassword,
//      role = UserRole.ARBITER.value
//    )
//    val expert = userRepository.getUser(login, hashedPassword)!!
//    experts.add(expert)
//  }
//
//  //создаем исследования
//  val researches = doseReports.map { jsonFileModel ->
//    createResearch(
//      researchData = jsonFileModel.ids!!,
//      researchRepository = researchRepository
//    )
//  }
//
//  val fullResearchesSize = researches.size
//  val expertsSize = experts.size
//  val researchSizePack = (fullResearchesSize / expertsSize).toInt()
//
////  delay(10000)
//
////  MY: fullResearchesSize = 635
////  MY: researchesMultiplier = 634
////  MY: expertsSize = 11
////  MY: researchSizePack = 57
//
//  for (i in 0 until (expertsSize - 1)) {
//    val fromIndex = i * researchSizePack
//    val toIndex = (i + 1) * researchSizePack
//    createUserResearchesRelationModel(
//      usersModels = listOf(experts[i]),
//      researches = researches.subList(fromIndex, toIndex),
//      repository = userResearchRepository
//    )
//  }
//
//  val fromIndex = (expertsSize - 1) * researchSizePack
//  val toIndex = researches.lastIndex + 1
//  createUserResearchesRelationModel(
//    usersModels = listOf(experts.last()),
//    researches = researches.subList(fromIndex, toIndex),
//    repository = userResearchRepository
//  )
//
//  createRois(
//    researches,
//    doseReports,
//    exportedRoisRepository,
//    expertMarksRepository,
//    userExpertMarkRepository,
//    tagger.id,
//    experts.map { it.id }
//  )
//}

private fun connectToDatabase() {
  Database.connect(
    url = "jdbc:mysql://localhost:3306/fantom_dose_report_exported?characterEncoding=utf8&useUnicode=true&useSSL=false",
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


