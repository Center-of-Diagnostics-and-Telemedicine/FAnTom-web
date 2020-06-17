package useCases

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route
import model.ErrorModel
import model.ErrorStringCode
import model.MarksModel
import model.MarksResponseNew
import repository.MarksRepository
import util.GetMarks
import util.user

fun Route.getMarks(
  markRepository: MarksRepository
) {

  get<GetMarks> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(MarksResponseNew(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    try {
      call.respond(MarksResponseNew(MarksModel(markRepository.getAll(user.id, it.id))))
    } catch (e: Exception) {
      application.log.error("Failed to get marks", e)
      respondError(ErrorStringCode.GET_MARKS_FAILED)
    }
  }
}
