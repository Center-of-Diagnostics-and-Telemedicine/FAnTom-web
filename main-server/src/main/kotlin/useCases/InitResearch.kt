package useCases

import io.ktor.application.*
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import model.ApiResponse
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
      call.respond(ApiResponse.ErrorResponse(errorCode.value))
    }

    val userId = call.user.id
    val research = researchRepository.getResearch(it.id)
    if (research == null) respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
    debugLog("research found")

    val sessionToClose = sessionRepository.getSession(userId)
    try {
      if (sessionToClose != null) {
        debugLog("found existing session for user, going to close it")
        sessionRepository.deleteSession(userId, research!!.accessionNumber)
      }

      val session = sessionRepository.createSession(userId, research!!.accessionNumber)
      debugLog("session created")
      call.respond(session.initResearch(research.accessionNumber))
      debugLog("session created")
    } catch (e: Exception) {
      application.log.error("Failed to init research", e)
      e.printStackTrace()
      respondError(ErrorStringCode.RESEARCH_INITIALIZATION_FAILED)
    }

  }
}