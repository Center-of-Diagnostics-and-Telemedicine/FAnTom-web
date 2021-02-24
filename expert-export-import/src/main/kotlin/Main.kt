import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import model.JSON_TYPE
import model.protocolsPath
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@OptIn(UnstableDefault::class)
fun main() {
//  connectToDatabase()
//
//  initDb()


  val jsonFileModels = getFiles(protocolsPath)
  val doctorIds = jsonFileModels
  val doctors = jsonFileModels.map { it.doctors.map { it.id } }.flatten().distinct()


  createDoctors(doctors)
  createMarks()

  val a = 1

  //достаем json в объекты
  //создаем записи простых отметок
}

fun createMarks() {
}

fun createDoctors(doctors: List<String>) {
  transaction {

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

fun getFileExtension(fullName: String?): String? {
  checkNotNull(fullName)
  val fileName = File(fullName).name
  val dotIndex = fileName.lastIndexOf('.')
  return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
}


