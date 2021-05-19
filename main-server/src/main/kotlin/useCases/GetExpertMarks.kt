package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.ResearchRepository
import repository.ExpertMarksRepository
import repository.UserExpertMarkRepository
import util.ExpertMarks
import util.user

fun Route.getExpertMarks(
  repository: ExpertMarksRepository,
  researchRepository: ResearchRepository,
  userExpertMarkRepository: UserExpertMarkRepository
) {

  get<ExpertMarks> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ExpertMarksResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val research = researchRepository.getResearch(it.researchId)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@get
    }

    try {
      val userExpertMarks = userExpertMarkRepository.getUserExpertMarks(
        userId = user.id,
        researchId = research.id
      )

      val entities = userExpertMarks.mapNotNull { userExpertMarkModel ->
        repository.get(userExpertMarkModel.markId)?.toExpertMarkEntity()
      }

      when (entities) {
        null -> call.respond(ExpertMarksResponse(response = emptyList()))
        else -> call.respond(ExpertMarksResponse(response = entities))
      }

    } catch (e: Exception) {
      application.log.error("Failed to get marks", e)
      respondError(ErrorStringCode.GET_MARKS_FAILED)
    }
  }

}