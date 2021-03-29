package useCase

import kotlinx.serialization.json.Json
import kotlinx.serialization.toUtf8Bytes
import model.JSON_TYPE
import model.JsonFileModel
import model.macProtocolsPath
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun getDoseReports(protocolsPath: String): List<JsonFileModel> {
  val dirs = File(protocolsPath).listFiles()
  val files = dirs.mapNotNull { it.listFiles()?.toList() }.flatten()
  val jsonFiles = files.filter { getFileExtension(it.name)?.toLowerCase() == JSON_TYPE }
  return jsonFiles.map { file ->
    var string = String(Files.readAllBytes(Paths.get(file.absolutePath)), Charsets.UTF_16LE)
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