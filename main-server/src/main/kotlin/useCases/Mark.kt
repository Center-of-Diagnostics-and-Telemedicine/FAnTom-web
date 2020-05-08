package useCases

import io.ktor.application.*
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.MarkRepository
import util.*

fun Route.mark(
  markRepository: MarkRepository
) {

  post<Mark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ErrorModel(errorCode.value))
    }

    val user = call.user
    val markModel = call.receive<ConfirmCTTypeRequest>().toMarkModel(user.id)
    try {
      markRepository.createMark(markModel)
      call.respond(OK)
    } catch (e: Exception) {
      application.log.error("Failed to createMark", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}