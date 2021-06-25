package useCases

import debugLog
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.delay
import model.ErrorModel
import model.ErrorStringCode
import model.ResearchInitResponse
import model.ResearchInitResponseNew
import repository.repository.ResearchRepository
import repository.repository.SessionRepository
import repository.repository.UserResearchRepository
import util.InitResearch
import util.user
import java.net.ConnectException

fun Route.initResearch(
  researchRepository: ResearchRepository,
  sessionRepository: SessionRepository,
  userResearchRepository: UserResearchRepository
) {

  get<InitResearch> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ResearchInitResponse(error = ErrorModel(errorCode.value)))
    }

    val userId = call.user.id
    val research = researchRepository.getResearch(it.id)
    if (research == null) respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
    debugLog("research found")

    userResearchRepository.markSeen(userId, research!!.id)

    val sessionToClose = sessionRepository.getSession(userId)
    debugLog("session == null : ${sessionToClose == null}")
    if (sessionToClose != null) {
      debugLog("found existing session for user, going to close it")
      try {
        sessionRepository.deleteSession(userId)
      } catch (e: Exception) {
      }
    }

    try {
      val session = sessionRepository.createSession(userId, research.accessionNumber)
      debugLog("session created")
      debugLog("delaying for 2secs")
      delay(2000)
      val response = session.initResearch(research.accessionNumber)
      call.respond(ResearchInitResponseNew(response = response))
    } catch (e: Exception) {
      application.log.error("Failed to init research", e)
      if (e is ConnectException) {
        respondError(ErrorStringCode.SESSION_EXPIRED)
      } else {
        respondError(ErrorStringCode.RESEARCH_INITIALIZATION_FAILED)
      }
    }

  }
}
