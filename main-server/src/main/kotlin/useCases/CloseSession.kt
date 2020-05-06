package useCases

import io.ktor.application.*
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import model.BaseResponse
import model.ErrorStringCode
import repository.ResearchRepository
import repository.SessionRepository
import util.*

fun Route.closeSession(
  researchRepository: ResearchRepository,
  sessionRepository: SessionRepository
) {

  get<CloseSession> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(errorCode.value))
    }

    val userId = call.user.id

    val research = researchRepository.getResearch(it.id)
    if (research == null) respondError(ErrorStringCode.RESEARCH_NOT_FOUND)

    val session = sessionRepository.getSession(userId, research!!.accessionNumber)
    if (session == null) respondError(ErrorStringCode.SESSION_EXPIRED)

    try {
      session?.deleteSession(userId, research.accessionNumber)
    } catch (e: Exception) {
      application.log.error("Failed to close session", e)
      respondError(ErrorStringCode.SESSION_CLOSE_FAILED)
    }
  }
}