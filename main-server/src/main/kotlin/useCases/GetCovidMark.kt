package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.CovidMarksRepository
import repository.ResearchRepository
import util.CovidMark
import util.user

fun Route.getCovidMark(
  covidMarksRepository: CovidMarksRepository,
  researchRepository: ResearchRepository
) {

  get<CovidMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(CovidMarksResponse(error = ErrorModel(errorCode.value)))
    }

    val user = call.user
    val research = researchRepository.getResearch(it.id)

    if (research == null) {
      respondError(ErrorStringCode.RESEARCH_NOT_FOUND)
      return@get
    }

    try {
      val mark = covidMarksRepository.getMark(user.id, it.id)

      when (mark) {
        null -> call.respond(CovidMarksResponse(response = getEmptyCovidMarkEntity()))
        else -> call.respond(CovidMarksResponse(response = mark.toCovidMarkEntity()))
      }

    } catch (e: Exception) {
      application.log.error("Failed to get marks", e)
      respondError(ErrorStringCode.GET_MARKS_FAILED)
    }
  }

}