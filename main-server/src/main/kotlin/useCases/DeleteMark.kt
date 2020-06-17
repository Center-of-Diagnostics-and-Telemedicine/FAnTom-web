package useCases

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.locations.delete
import io.ktor.locations.put
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.MarksRepository
import util.DeleteMark

fun Route.deleteMark(
  markRepository: MarksRepository
) {

  delete<DeleteMark> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(BaseResponse(error = ErrorModel(errorCode.value)))
    }

    val mark = call.receive<MarkDomain>()
    try {
      markRepository.delete(it.id)
      call.respond(BaseResponse(OK()))
    } catch (e: Exception) {
      application.log.error("Failed to delete mark", e)
      respondError(ErrorStringCode.DELETE_MARK_FAILED)
    }
  }
}
