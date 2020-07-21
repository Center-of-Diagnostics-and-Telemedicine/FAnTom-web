package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
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
    val request = call.receive<SliceRequestNew>()
    val research = researchRepository.getResearch(it.id)
    when {
      research == null -> respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      request.image.number < 0 -> respondError(ErrorStringCode.INCORRECT_SLICE_NUMBER)
    }

    val existingSession = sessionRepository.getSession(userId)
    if (existingSession == null) {
      respondError(ErrorStringCode.SESSION_EXPIRED)
      return@post
    }

    try {
      call.respond(
        SliceResponse(SliceModel(existingSession.getSlice(request, research!!.accessionNumber)))
      )
    } catch (e: Exception) {
      application.log.error("Failed to get slice", e)
      respondError(ErrorStringCode.GET_SLICE_FAILED)
    }
  }
}