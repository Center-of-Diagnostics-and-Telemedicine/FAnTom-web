package useCases

import io.ktor.application.*
import io.ktor.locations.get
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

    val params = call.request.queryParameters
    val axialCoord = params[TYPE_AXIAL]?.toInt()
    val frontalCoord = params[TYPE_FRONTAL]?.toInt()
    val sagittalCoord = params[TYPE_SAGITTAL]?.toInt()

    when {
      it.name.isEmpty() -> respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      axialCoord == null || axialCoord < 0 -> respondError(ErrorStringCode.INCORRECT_AXIAL_COORD)
      frontalCoord == null || frontalCoord < 0 -> respondError(ErrorStringCode.INCORRECT_FRONTAL_COORD)
      sagittalCoord == null || sagittalCoord < 0 -> respondError(ErrorStringCode.INCORRECT_SAGITTAL_COORD)
    }

    val existingSession = sessionRepository.getSession(userId)
    if (existingSession == null) respondError(ErrorStringCode.SESSION_EXPIRED)

    try {
      call.respond(
        HounsfieldResponse(
          HounsfieldModel(
            existingSession!!.hounsfield(
              axialCoord!!,
              frontalCoord!!,
              sagittalCoord!!
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