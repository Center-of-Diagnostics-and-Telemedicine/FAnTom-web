package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import model.*
import model.toCovidMarkModel
import repository.repository.CovidMarksRepository
import util.CovidMark
import util.user

fun Route.mark(
  covidMarksRepository: CovidMarksRepository
) {

  post<CovidMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markEntity = call.receive<CovidMarkEntity>()
    val markModel = markEntity.toCovidMarkModel(user.id, it.id)

    try {
      val existing =
        covidMarksRepository.getMark(userId = user.id, researchId = markModel.researchId)
      if (existing == null) {
        covidMarksRepository.createMark(markModel)
      } else {
        covidMarksRepository.updateMark(markModel)
      }
      call.respond(CovidMarksResponse(response = markEntity))
    } catch (e: Exception) {
      application.log.error("Failed to create/update CovidMark", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}
