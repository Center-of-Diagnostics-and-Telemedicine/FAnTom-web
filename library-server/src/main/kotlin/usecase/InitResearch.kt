package usecase

import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import model.ApiResponse
import model.ErrorStringCode
import repository.ResearchRepository
import util.InitResearch
import util.NotInitializedYetException

fun Route.initResearch(
  researchRepository: ResearchRepository
) {

  get<InitResearch> {

    suspend fun respondError(errorCode: ErrorStringCode, message: String = "") {
      call.respond(ApiResponse.ErrorResponse(errorCode.value, message))
    }

    try {
      call.respond(researchRepository.initResearch(it.accessionName))
    } catch (e: Exception) {
      when (e) {
        is NotInitializedYetException -> respondError(
          ErrorStringCode.NOT_INITIALIZED_YET,
          e.myMessage
        )
        else -> respondError(ErrorStringCode.RESEARCH_INITIALIZATION_FAILED)
      }
    }
  }
}