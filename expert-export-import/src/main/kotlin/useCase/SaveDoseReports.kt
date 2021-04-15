package useCase

import kotlinx.serialization.json.Json
import model.JsonFileModel
import java.io.File

fun saveDoseReports(destinationPath: String, doseReports: List<JsonFileModel>) {
  doseReports.forEach {
    val file = File("${destinationPath}/${it.ids!!.accessionNumber}_${it.ids!!.studyId}.json")
    file.writeText(Json.stringify(JsonFileModel.serializer(), it))
  }
}