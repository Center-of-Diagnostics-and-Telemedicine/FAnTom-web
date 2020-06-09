package usecase

import io.ktor.application.call
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.ResearchRepository
import util.GetSlice

@ExperimentalStdlibApi
fun Route.getSlice(
  researchRepository: ResearchRepository
) {

  post<GetSlice> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ErrorModel(errorCode.value))
    }

    try {
      val params = call.receive<SliceRequest>()
      call.respond(SliceResponse(SliceModel(researchRepository.getSlice(params))))
    } catch (e: Exception) {
      respondError(ErrorStringCode.GET_SLICE_FAILED)
    }
  }
}
