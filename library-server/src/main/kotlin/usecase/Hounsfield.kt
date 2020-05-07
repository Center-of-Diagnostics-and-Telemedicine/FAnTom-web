package usecase

import io.ktor.application.call
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.ResearchRepository
import util.Hounsfield

fun Route.hounsfield(
  researchRepository: ResearchRepository
) {

  post<Hounsfield> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ApiResponse.ErrorResponse(errorCode.value))
    }

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

    try {
      val huValue = researchRepository.hounsfield(axialCoord!!, frontalCoord!!, sagittalCoord!!)
      call.respond(ApiResponse.HounsfieldResponse(huValue))
    } catch (e: Exception) {
      respondError(ErrorStringCode.GET_SLICE_FAILED)
    }
  }
}