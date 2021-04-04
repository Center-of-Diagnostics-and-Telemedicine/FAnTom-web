import model.UserModel
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
  val tagger = userRepository.getUser("#tagger_01", hash("text_detector_v01"))!!

  val arbiterLogin = "arbiter"
  val arbiterPassword = "doseReportsArbiter"
  val experts = mutableListOf<UserModel>()
  for (i in 0..10) {
    val login = "$arbiterLogin$i"
    val hashedPassword = hash("$arbiterPassword$i")
    userRepository.createUser(
      login = login,
      hashedPassword = hashedPassword,
      role = UserRole.ARBITER.value
    )
    val expert = userRepository.getUser(login, hashedPassword)!!
    experts.add(expert)
  }

  //создаем исследования
  val researches = doseReports.map { jsonFileModel ->
    createResearch(
      researchData = jsonFileModel.ids!!,
      researchRepository = researchRepository
    )
  }

  createUserResearchesRelationModel(
    usersModels = experts,
    researches = researches,
    repository = userResearchRepository
  )

  createRois(
    researches,
    doseReports,
    exportedRoisRepository,
    exportedMarksRepository,
    userExpertMarkRepository,
    tagger.id,
    experts.map { it.id }
  )
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


