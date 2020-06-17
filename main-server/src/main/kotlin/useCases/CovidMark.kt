package useCases

import io.ktor.application.*
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.CovidMarkRepository
import util.*
import util.CovidMark

fun Route.mark(
  covidMarkRepository: CovidMarkRepository
) {

  post<CovidMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<ConfirmCTTypeRequest>().toMarkModel(user.id)
    try {
      val existing = covidMarkRepository.getMark(userId = user.id, researchId = markModel.researchId)
      if (existing == null) {
        covidMarkRepository.createMark(markModel)
      } else {
        covidMarkRepository.updateMark(markModel)
      }
      call.respond(BaseResponse(response = OK()))
    } catch (e: Exception) {
      application.log.error("Failed to createMark", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}
