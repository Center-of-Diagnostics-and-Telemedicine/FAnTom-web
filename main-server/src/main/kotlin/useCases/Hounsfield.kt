package useCases

import io.ktor.application.*
import io.ktor.locations.get
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.SessionRepository
import util.Hounsfield
import util.user

fun Route.hounsfield(sessionRepository: SessionRepository) {

  get<Hounsfield> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(HounsfieldResponse(error = ErrorModel(errorCode.value)))
    }

    val userId = call.user.id
    val params = call.receive<HounsfieldRequest>()

    when {
      params.axialCoord < 0 -> respondError(ErrorStringCode.INCORRECT_AXIAL_COORD)
      params.frontalCoord < 0 -> respondError(ErrorStringCode.INCORRECT_FRONTAL_COORD)
      params.sagittalCoord < 0 -> respondError(ErrorStringCode.INCORRECT_SAGITTAL_COORD)
    }

    val existingSession = sessionRepository.getSession(userId)
    if (existingSession == null) respondError(ErrorStringCode.SESSION_EXPIRED)

    try {
      call.respond(
        HounsfieldResponse(
          HounsfieldModel(
            existingSession!!.hounsfield(
              params.axialCoord,
              params.frontalCoord,
              params.sagittalCoord
            )
          )
        )
      )
    } catch (e: Exception) {
      application.log.error("Failed to get hounsfield", e)
      respondError(ErrorStringCode.HOUNSFIELD_ERROR)
    }
  }
}