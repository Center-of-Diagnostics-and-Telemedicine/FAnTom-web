package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.CovidMarkRepository
import util.CovidMark
import util.user

fun Route.mark(
  covidMarkRepository: CovidMarkRepository
) {

  post<CovidMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<CovidMarkEntity>().toCovidMarkModel(user.id, it.id)

    try {
      val existing =
        covidMarkRepository.getMark(userId = user.id, researchId = markModel.researchId)
      if (existing == null) {
        covidMarkRepository.createMark(markModel)
      } else {
        covidMarkRepository.updateMark(markModel)
      }
      call.respond(BaseResponse(response = OK()))
    } catch (e: Exception) {
      application.log.error("Failed to create/update CovidMark", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}
