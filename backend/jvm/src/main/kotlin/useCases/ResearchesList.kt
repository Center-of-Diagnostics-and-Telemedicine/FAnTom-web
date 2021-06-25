package useCases

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import model.*
import repository.repository.CovidMarksRepository
import repository.repository.ResearchRepository
import repository.repository.UserResearchRepository
import util.ResearchesList
import util.user

fun Route.researchesList(
  researchRepository: ResearchRepository,
  userResearchRepository: UserResearchRepository,
  marksRepositoryCovid: CovidMarksRepository
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
              modality = research.modality,
              category = research.category
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
