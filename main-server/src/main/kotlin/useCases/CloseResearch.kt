package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.ResearchRepository
import repository.SessionRepository
import repository.UserResearchRepository
import util.CloseResearch
import util.user

fun Route.closeResearch(
  researchRepository: ResearchRepository,
  userResearchRepository: UserResearchRepository
) {

  get<CloseResearch> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val userId = call.user.id
    val research = researchRepository.getResearch(it.id)

    if(research == null){
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@get
    }

    try {
      userResearchRepository.updateUserResearch(
        UserResearchModel(
          userId = userId,
          researchId = research.id,
          seen = true,
          done = true
        )
      )
      call.respond(BaseResponse(response = OK()))
    } catch (e: Exception) {
      application.log.error("Failed to close session", e)
      respondError(ErrorStringCode.RESEARCH_CLOSE_FAILED)
    }
  }
}