package useCase

import kotlinx.serialization.json.Json
import model.JSON_TYPE
import model.JsonFileModel
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

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

private fun getFileExtension(fullName: String?): String? {
  checkNotNull(fullName)
  val fileName = File(fullName).name
  val dotIndex = fileName.lastIndexOf('.')
  return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
}