package client.newmvi.researchmvi.store

import client.ResearchApiExceptions
import client.domain.repository.ResearchRepository
import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.*
import model.RESEARCH_INITIALIZATION_FAILED
import model.ResearchSlicesSizesData

interface ResearchDataLoader {

  @EventsOnAnyScheduler
  fun load(researchId: Int): Single<Result>

  sealed class Result {
    class Success(val researchData: ResearchSlicesSizesData) : Result()
    object SessionExpired : Result()
    data class Error(val message: String) : Result()
  }
}

class ResearchDataLoaderImpl(
  private val repository: ResearchRepository
) : ResearchDataLoader {

  override fun load(researchId: Int): Single<ResearchDataLoader.Result> =
    singleFromCoroutine {
      repository.initResearch(researchId)
    }
      .map {
        val result = it.copy(researchId = researchId)
        result
      }
      .map(ResearchDataLoader.Result::Success)
      .onErrorResumeNext {
        when (it) {
          is ResearchApiExceptions.ResearchInitializationException ->
            ResearchDataLoader.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.ResearchNotFoundException ->
            ResearchDataLoader.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.SessionExpiredException ->
            ResearchDataLoader.Result.SessionExpired.toSingle()

          else -> ResearchDataLoader.Result.Error(RESEARCH_INITIALIZATION_FAILED).toSingle()
        }
      }
}