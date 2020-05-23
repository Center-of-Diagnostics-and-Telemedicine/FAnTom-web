package usecase

import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import lib.MarkTomogrammObject
import model.ErrorModel
import model.ErrorStringCode
import model.ResearchInitResponse
import model.localDataStorePath
import repository.ResearchRepository
import util.InitResearch
import util.LibraryState
import util.NotInitializedYetException

fun Route.initResearch(
  researchRepository: ResearchRepository
) {

  get<InitResearch> {

    suspend fun respondError(errorCode: ErrorStringCode, message: String = "") {
      call.respond(ResearchInitResponse(error = ErrorModel(errorCode.value, message)))
    }

    try {
      when (MarkTomogrammObject.state) {
        LibraryState.ReadyToInitLib -> {
          researchRepository.initLib("$localDataStorePath\\+AGFA000000015851_AGFA000000015807")
          researchRepository.initResearch(it.name)
          call.respond(ResearchInitResponse(researchRepository.getInitialData()))
        }
        LibraryState.ReadyToInitResearch -> {
          researchRepository.initResearch(it.name)
          call.respond(ResearchInitResponse(researchRepository.getInitialData()))
        }
        LibraryState.ResearchInitialized -> {
          call.respond(ResearchInitResponse(researchRepository.getInitialData()))
        }
        LibraryState.InitLibProcess -> {
          respondError(ErrorStringCode.NOT_INITIALIZED_YET, "init lib started")
        }
        LibraryState.InitResearchProcess -> {
          respondError(ErrorStringCode.NOT_INITIALIZED_YET, "init study in process")
        }
      }
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
