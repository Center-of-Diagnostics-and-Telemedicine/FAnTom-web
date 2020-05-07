package usecase

import io.ktor.application.call
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.ResearchRepository
import util.GetSlice

fun Route.getSlice(
  researchRepository: ResearchRepository
) {

  post<GetSlice> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ApiResponse.ErrorResponse(errorCode.value))
    }

    try {
      val params = call.receive<SliceRequest>()
      val byteArray = researchRepository.getSlice(params)
      call.respond(ApiResponse.SliceResponse(byteArray))
    } catch (e: Exception) {
      respondError(ErrorStringCode.GET_SLICE_FAILED)
    }
  }
}