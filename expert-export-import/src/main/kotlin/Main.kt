import kotlinx.serialization.json.Json
import model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import useCase.createDoctors
import useCase.createMarks
import useCase.createResearch
import useCase.createUsersResearchRelationModel
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

suspend fun main() {
//  connectToDatabase()
//
//  initDb()


//  import()
  export()
}

private suspend fun export() {
  val researchId = 308
  val userIds = userResearchRepository.getUsersForResearch(researchId).map { it.userId }
  val marks = userIds.map { it to multiPlanarMarksRepository.getAll(it, researchId) }
  val comments = marks.map { pair ->
    val (userId, marks) = pair

  }
  val doctorsModels =


  val researches = userIds.mapNotNull { researchRepository.getResearch(it.researchId) }
  val researchMarksMap = researches.map { it to multiPlanarMarksRepository.getAll(userId, it.id) }
  val comments = researchMarksMap.forEach { pair ->
    val (researchModel, marks) = pair

  }
  val files = researches.map {
    JsonFileModel(
      doctorComments = listOf()
    )
  }

}

private suspend fun import() {
  val jsonFileModels = getFiles(protocolsPath)
  jsonFileModels.forEach { jsonModel ->
    val doctorIds = jsonModel.doctorComments.map { it.id }
    val users = createDoctors(
      doctorsIds = doctorIds,
      userRepository = userRepository
    )
    val research = createResearch(
      researchData = jsonModel.ids,
      researchRepository = researchRepository
    )
    val userResearches = createUsersResearchRelationModel(
      userModels = users,
      researchModels = listOf(research),
      repository = userResearchRepository
    )
    val userToNodule = mapNodules(jsonModel, users)

    createMarks(
      usersToNodules = userToNodule,
      users = users,
      research = research,
      repository = exportedMarksRepository
    )
  }
}

fun getFiles(protocolsPath: String): List<JsonFileModel> {
  val files = File(protocolsPath).listFiles()
  val jsonFiles = files.filter { getFileExtension(it.name)?.toLowerCase() == JSON_TYPE }
  return jsonFiles.map { file ->
    val string = String(Files.readAllBytes(Paths.get(file.absolutePath)))
    val sanitized: String = string
      .replace("[\uFEFF-\uFFFF]", "")
      .replace("\t", "")
      .replace("\r", "")
      .replace("\n", "")
      .replace(" ", "")
    val startPosition = sanitized.indexOfFirst { it == '{' }
    val message = sanitized.substring(startPosition)
    Json.parse(JsonFileModel.serializer(), message)
  }
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
      ExpertMarksVos
    )
    SchemaUtils.createMissingTablesAndColumns(
      ExpertMarksVos
    )
  }

}


