package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import model.*
import model.toExpertMarkModel
import repository.ExpertMarksRepository
import repository.ResearchRepository
import repository.UserExpertMarkRepository
import util.ExpertMark
import util.user

fun Route.saveExpertMark(
  repository: ExpertMarksRepository,
  researchRepository: ResearchRepository,
  userExpertMarkRepository: UserExpertMarkRepository
) {

  post<ExpertMark> {

    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ExpertMarksResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val markModel = call.receive<ExpertMarkEntity>()
    val research = researchRepository.getResearch(it.researchId)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@post
    }

    try {

      val mark = repository.create(markModel.toExpertMarkModel(), user.id, it.researchId)
      if (mark == null) {
        respondError(ErrorStringCode.CREATE_MARK_FAILED)
        return@post
      }
      userExpertMarkRepository.createUserExpertMark(
        UserExpertMarkModel(
          userId = user.id,
          researchId = research.id,
          markId = mark.id
        )
      )
      call.respond(ExpertMarksResponse(response = listOf(mark.toExpertMarkEntity())))

    } catch (e: Exception) {
      application.log.error("Failed to createRoi", e)
      respondError(ErrorStringCode.CREATE_MARK_FAILED)
    }
  }
}