package usecase

import io.ktor.application.call
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import lib.MarkTomogrammObject
import model.*
import repository.ResearchRepository
import util.*

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
          researchRepository.initLib(data_store_path)
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