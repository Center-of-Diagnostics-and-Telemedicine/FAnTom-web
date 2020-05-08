package useCases

import io.ktor.application.*
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import kotlinx.coroutines.delay
import model.*
import repository.ResearchRepository
import repository.SessionRepository
import util.*

fun Route.initResearch(
  researchRepository: ResearchRepository,
  sessionRepository: SessionRepository
) {

  get<InitResearch> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ResearchInitResponse(error = ErrorModel(errorCode.value)))
    }

    val userId = call.user.id
    val research = researchRepository.getResearch(it.id)
    if (research == null) respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
    debugLog("research found")

    val sessionToClose = sessionRepository.getSession(userId)
    debugLog("session == null : ${sessionToClose == null}")
    if (sessionToClose != null) {
      debugLog("found existing session for user, going to close it")
      try {
        sessionRepository.deleteSession(userId, research!!.accessionNumber)
      } catch (e: Exception) {
      }
    }
    try {
      val session = sessionRepository.createSession(userId, research!!.accessionNumber)
      debugLog("session created")
      debugLog("delaying for 2secs")
      delay(2000)
      val response = session.initResearch(research.accessionNumber)
      call.respond(ResearchInitResponse(response = response))
    } catch (e: Exception) {
      application.log.error("Failed to init research", e)
      e.printStackTrace()
      respondError(ErrorStringCode.RESEARCH_INITIALIZATION_FAILED)
    }

  }
}