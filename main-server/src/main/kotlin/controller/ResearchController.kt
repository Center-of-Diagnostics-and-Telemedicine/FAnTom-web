package controller

import fantom.SessionManager
import getResearch
import getResearches
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import model.*
import saveCtTypeConfirmation
import updateResearch
import util.*

class ResearchController {

  suspend fun init(call: ApplicationCall) {
    val id = call.parameters[ID_FIELD]
    if (id != null && id.isNotEmpty()) {
      debugLog("id is: $id")
      val userId = call.user.id
      val research = getResearch(parseInt(id), userId)
      if (research == null) {
        call.respond(HttpStatusCode.NotFound)
      } else {
        try {
          val researchName = research.name
          debugLog("calling init in backend with researchName = $researchName")
          val instance = SessionManager.getInstanceForUser(
            userId = userId, researchName = researchName
          )
          if (instance == null) {
            respondError(call)
          } else {
            val initResearch = instance.initResearch(researchName)
            updateResearch(research.copy(seen = true), userId = userId)
            call.respond(initResearch)
          }
        } catch (e: Exception) {
          e.printStackTrace()
          respondError(call)
        }
      }
    } else {
      call.respond(HttpStatusCode.NotFound)
    }
  }

  suspend fun list(call: ApplicationCall) {
    call.respond(ResearchesResponse(getResearches(call.user.id)))
  }

  suspend fun getSlice(call: ApplicationCall) {
    val id = call.parameters[ID_FIELD]
    if (id != null && id.isNotEmpty()) {
      val research = getResearch(parseInt(id), call.user.id)
      if (research == null) {
        call.respond(HttpStatusCode.NotFound)
      } else {
        val request = call.receive<SliceRequest>()
        val instanceForUser = SessionManager
          .getInstanceForUser(call.user.id, researchName = research.name)
        if (instanceForUser != null) {
          val slice = instanceForUser
            .getSlice(sliceRequest = request, researchName = research.name)
          call.respond(slice)
        } else {
          respondError(call)
        }
      }
    }
  }

  suspend fun hounsfield(call: ApplicationCall) {
    val params = call.request.queryParameters
    val axialCoord = params[TYPE_AXIAL]?.toInt() ?: 0
    val frontalCoord = params[TYPE_FRONTAL]?.toInt() ?: 0
    val sagittalCoord = params[TYPE_SAGITTAL]?.toInt() ?: 0

    debugLog("axial = $axialCoord, frontal = $frontalCoord, sagital = $sagittalCoord")

    val instanceForUser = SessionManager
      .getInstanceForUser(call.user.id, "")
    if (instanceForUser != null) {
      val value = instanceForUser
        .getHounsfield(
          axialCoord,
          frontalCoord,
          sagittalCoord
        )

      call.respond(ApiResponse.HounsfieldResponse(value))
    } else {
      respondError(call)
    }
  }

  suspend fun close(call: ApplicationCall) {
    val id = call.parameters[ID_FIELD]
    val request = call.receive<ConfirmCTTypeRequest>()
    if (id != null && id.isNotEmpty()) {
      debugLog("id is: $id")
      val research = getResearch(parseInt(id), call.user.id)
      if (research == null) {
        call.respond(HttpStatusCode.NotFound)
      } else {
        try {
          saveCtTypeConfirmation(request, call.user.id)
          updateResearch(research.copy(done = true), userId = call.user.id)
          call.respond(HttpStatusCode.OK)
        } catch (e: Exception) {
          e.printStackTrace()
          respondError(call)
        }
      }
    } else {
      call.respond(HttpStatusCode.NotFound)
    }
  }

  private suspend fun respondError(call: ApplicationCall) {
    call.respond(
      status = HttpStatusCode.InternalServerError,
      message = "Произошла ошибка"
    )
  }

}