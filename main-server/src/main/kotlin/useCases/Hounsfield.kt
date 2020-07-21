package useCases

import io.ktor.application.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import model.*
import repository.SessionRepository
import util.Hounsfield
import util.user

fun Route.hounsfield(sessionRepository: SessionRepository) {

  post<Hounsfield> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(HounsfieldResponse(error = ErrorModel(errorCode.value)))
    }

    val userId = call.user.id
    val params = call.receive<HounsfieldRequestNew>()

    val existingSession = sessionRepository.getSession(userId)
    if (existingSession == null) {
      respondError(ErrorStringCode.SESSION_EXPIRED)
      return@post
    }

    try {
      call.respond(HounsfieldResponse(HounsfieldModel(existingSession.hounsfield(params))))
    } catch (e: Exception) {
      application.log.error("Failed to get hounsfield", e)
      respondError(ErrorStringCode.HOUNSFIELD_ERROR)
    }
  }
}