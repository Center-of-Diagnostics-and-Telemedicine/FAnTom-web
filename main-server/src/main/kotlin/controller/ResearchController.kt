package controller

import fantom.SessionManager
import util.*
import getResearch
import getResearches
import model.*
import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import updateResearch

class ResearchController {

  suspend fun init(call: ApplicationCall) {
    val id = call.parameters[ID]
    if (id != null && id.isNotEmpty()) {
      debugLog("id is: $id")
      val userId = call.user.id
      val research = getResearch(parseInt(id), userId)
      try {
        val response = SessionManager.getInstanceForUser(userId = userId).initResearch(research.name)
        updateResearch(research.copy(seen = true), userId = userId)
        call.respond(response)
      } catch (e: Exception) {
        e.printStackTrace()
        respondError(call)
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
//      debugLog("researchId is: $id")
      val request = call.receive<SliceRequest>()
      val slice = SessionManager
        .getInstanceForUser(call.user.id)
        .getSlice(sliceRequest = request, researchId = id)
      if (slice != null) {
        call.respond(slice)
      } else {
        respondError(call)
      }
    }
  }

  suspend fun hounsfield(call: ApplicationCall) {
    val params = call.request.queryParameters
    val axialCoord = params[TYPE_AXIAL]?.toInt() ?: 0
    val frontalCoord = params[TYPE_FRONTAL]?.toInt() ?: 0
    val sagittalCoord = params[TYPE_SAGITTAL]?.toInt() ?: 0

    debugLog("axial = $axialCoord, frontal = $frontalCoord, sagital = $sagittalCoord")

    val value = SessionManager
      .getInstanceForUser(call.user.id)
      .getHounsfield(
        axialCoord,
        frontalCoord,
        sagittalCoord
      )

    call.respond(HounsfieldResponse(value))
  }

  suspend fun close(call: ApplicationCall) {
    val id = call.parameters[ID_FIELD]
    if (id != null && id.isNotEmpty()) {
      debugLog("id is: $id")
      val research = getResearch(parseInt(id), call.user.id)
      try {
        updateResearch(research.copy(done = true), userId = call.user.id)
        call.respond(HttpStatusCode.OK)
      } catch (e: Exception) {
        e.printStackTrace()
        respondError(call)
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