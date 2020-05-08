package useCases

import io.ktor.application.*
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.ResearchRepository
import repository.SessionRepository
import util.GetSlice
import util.user

fun Route.getSlice(
  researchRepository: ResearchRepository,
  sessionRepository: SessionRepository
) {

  post<GetSlice> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(SliceResponse(error = ErrorModel(errorCode.value)))
    }

    val userId = call.user.id
    val request = call.receive<SliceRequest>()
    val research = researchRepository.getResearch(it.id)
    when {
      research == null -> respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      request.sliceNumber < 0 -> respondError(ErrorStringCode.INCORRECT_SLICE_NUMBER)
    }

    val existingSession = sessionRepository.getSession(userId)
    if (existingSession == null) respondError(ErrorStringCode.SESSION_EXPIRED)

    try {
      call.respond(
        SliceResponse(SliceModel(existingSession!!.getSlice(request, research!!.accessionNumber)))
      )
    } catch (e: Exception) {
      application.log.error("Failed to get slice", e)
      respondError(ErrorStringCode.GET_SLICE_FAILED)
    }
  }
}