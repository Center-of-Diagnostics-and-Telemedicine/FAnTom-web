package useCases

import io.ktor.application.*
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.*
import util.Mark
import util.user

fun Route.mark(
  markRepository: MarkRepository
) {

  post<Mark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ApiResponse.ErrorResponse(errorCode.value))
    }

    val user = call.user
    val markModel = call.receive<ConfirmCTTypeRequest>().toMarkModel(user.id)
    try {
      markRepository.createMark(markModel)
    } catch (e: Exception) {
      application.log.error("Failed to createMark", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}