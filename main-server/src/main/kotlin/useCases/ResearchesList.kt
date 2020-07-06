package useCases

import io.ktor.application.application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.CovidMarkRepository
import repository.ResearchRepository
import repository.UserResearchRepository
import util.ResearchesList
import util.user

fun Route.researchesList(
  researchRepository: ResearchRepository,
  userResearchRepository: UserResearchRepository,
  marksRepositoryCovid: CovidMarkRepository
) {

  get<ResearchesList> {
    try {
      val userResearches = userResearchRepository
        .getResearchesForUser(call.user.id)
        .mapNotNull {
          val marked = marksRepositoryCovid.getMark(call.user.id, it.researchId) != null
          val research = researchRepository.getResearch(it.researchId)
          if (research != null) {
            Research(
              id = it.researchId,
              name = research.accessionNumber,
              seen = it.seen,
              done = it.done,
              marked = marked,
              modality = research.modality
            )
          } else {
            null
          }
        }
      call.respond(ResearchesResponse(ResearchesModel(userResearches)))
    } catch (e: Exception) {
      application.log.error("Failed to get research list", e)
      call.respond(
        ResearchesResponse(error = ErrorModel(ErrorStringCode.USER_RESEARCHES_LIST_FAILED.value))
      )
    }
  }


}
