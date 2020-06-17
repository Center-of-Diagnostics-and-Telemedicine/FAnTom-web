package useCases

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.MarksRepository
import util.CreateMark
import util.user

fun Route.createMark(
  markRepository: MarksRepository
) {

  post<CreateMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(MarkResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<MarkData>()
    try {
      val mark = markRepository.create(markModel, user.id, it.id)
      if (mark != null) {
        call.respond(MarkResponse(MarkDomainModel(mark)))
      } else {
        respondError(ErrorStringCode.CREATE_MARK_FAILED)
      }
    } catch (e: Exception) {
      application.log.error("Failed to createMark", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}
