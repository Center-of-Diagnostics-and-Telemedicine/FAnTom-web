package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.BaseResponse
import model.ErrorModel
import model.ErrorStringCode
import model.OK
import repository.repository.SessionRepository
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
