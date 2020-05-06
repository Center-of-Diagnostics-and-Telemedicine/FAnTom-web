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

fun Route.initResearch(
  researchRepository: ResearchRepository,
  sessionRepository: SessionRepository
) {

  get<InitResearch> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(errorCode.value))
    }

    val userId = call.user.id
    val research = researchRepository.getResearch(it.id)
    if (research == null) respondError(ErrorStringCode.RESEARCH_NOT_FOUND)

    val existingSession = sessionRepository.getSession(userId, research!!.accessionNumber)
    if (existingSession != null) {
      sessionRepository.deleteSession(userId, research.accessionNumber)
    }
    val session = sessionRepository.createSession(userId, research.accessionNumber)

    try {
      session.initResearch(research.accessionNumber)
    } catch (e: Exception) {
      application.log.error("Failed to init research", e)
      e.printStackTrace()
      respondError(ErrorStringCode.RESEARCH_INITIALIZATION_FAILED)
    }

    try {
      call.respond(session.getResearchData(research.accessionNumber))
    } catch (e: Exception) {
      application.log.error("Failed to getResearchData", e)
      respondError(ErrorStringCode.RESEARCH_INITIALIZATION_FAILED)
    }
  }
}