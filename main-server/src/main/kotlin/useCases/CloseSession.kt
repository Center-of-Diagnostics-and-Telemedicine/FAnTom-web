package useCases

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import model.BaseResponse
import model.ErrorModel
import model.ErrorStringCode
import model.OK
import repository.ResearchRepository
import repository.SessionRepository
import util.CloseSession
import util.user

fun Route.closeSession(
  sessionRepository: SessionRepository
) {

  get<CloseSession> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val userId = call.user.id

    val session = sessionRepository.getSession(userId)
    if (session == null) {
      respondError(ErrorStringCode.SESSION_EXPIRED)
      return@get
    }

    try {
      sessionRepository.deleteSession(userId)
      call.respond(BaseResponse(response = OK()))
    } catch (e: Exception) {
      application.log.error("Failed to close session", e)
      respondError(ErrorStringCode.SESSION_CLOSE_FAILED)
    }
  }
}
