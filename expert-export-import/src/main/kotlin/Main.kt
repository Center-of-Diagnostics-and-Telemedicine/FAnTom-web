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

  //создаем пользователей
  userRepository.createUser(
    login = "#tagger_01",
    hashedPassword = hash("text_detector_v01"),
    role = UserRole.TAGGER.value
  )
  userRepository.createUser(
    login = "arbiter",
    hashedPassword = hash("doseReportsArbiter"),
    role = UserRole.ARBITER.value
  )
  val tagger = userRepository.getUser("#tagger_01", hash("text_detector_v01"))!!
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

  createRois(researches, doseReports, exportedRoisRepository, exportedMarksRepository, tagger.id)
}

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


