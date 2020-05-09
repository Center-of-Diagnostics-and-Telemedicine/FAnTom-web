package usecase

import io.ktor.application.call
import io.ktor.locations.post
import io.ktor.request.receive
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
      call.respond(HounsfieldResponse(error = ErrorModel(errorCode.value)))
    }

    val params = call.receive<HounsfieldRequest>()

    try {
      val huValue = researchRepository.hounsfield(
        params.axialCoord,
        params.frontalCoord,
        params.sagittalCoord
      )
      call.respond(HounsfieldResponse(HounsfieldModel(huValue)))
    } catch (e: Exception) {
      respondError(ErrorStringCode.GET_SLICE_FAILED)
    }
  }
}