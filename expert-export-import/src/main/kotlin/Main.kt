import kotlinx.serialization.json.Json
import model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import useCase.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

suspend fun main() {
//  connectToDatabase()
//
//  initDb()


  val jsonFileModels = getFiles(protocolsPath)
  jsonFileModels.forEach { jsonModel ->
    val doctorIds = jsonModel.doctors.map { it.id }
    val users = createDoctors(
      doctorsIds = doctorIds,
      userRepository = userRepository
    )
    val research = createResearch(
      researchData = jsonModel.ids,
      researchRepository = researchRepository
    )
    val userResearches = createUserResearches(
      userModels = users,
      researchModels = listOf(research),
      repository = userResearchRepository
    )
    val userToNodule = mapNodules(jsonModel, users)

    createMarks(
      usersToNodules = userToNodule,
      users = users,
      research,
      repository = exportedMarksRepository
    )
  }
}

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

fun getFileExtension(fullName: String?): String? {
  checkNotNull(fullName)
  val fileName = File(fullName).name
  val dotIndex = fileName.lastIndexOf('.')
  return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
}


