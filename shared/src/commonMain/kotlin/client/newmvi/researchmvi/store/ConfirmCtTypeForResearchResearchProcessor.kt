package client.newmvi.researchmvi.store

import client.ResearchApiExceptions
import client.domain.repository.ResearchRepository
import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.single.*
import model.CREATE_MARK_FAILED
import model.CTType

interface ConfirmCtTypeForResearchResearchProcessor {

  @EventsOnAnyScheduler
  fun confirm(ctType: CTType, leftPercent: Int, rightPercent: Int, researchId: Int): Single<Result>

  sealed class Result {
    object Success : Result()
    object SessionExpired : Result()
    data class Error(val message: String) : Result()
  }
}

class ConfirmCtTypeForResearchResearchProcessorImpl(
  val repository: ResearchRepository
) : ConfirmCtTypeForResearchResearchProcessor {

  override fun confirm(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  ): Single<ConfirmCtTypeForResearchResearchProcessor.Result> =
    singleFromCoroutine {
      repository.confirmCtTypeForResearch(ctType, leftPercent, rightPercent, researchId)
    }
      .map { ConfirmCtTypeForResearchResearchProcessor.Result.Success }
      .onErrorResumeNext {
        when (it) {
          is ResearchApiExceptions.ConfirmCtTypeForResearchException ->
            ConfirmCtTypeForResearchResearchProcessor.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.ResearchNotFoundException ->
            ConfirmCtTypeForResearchResearchProcessor.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.SessionExpiredException ->
            ConfirmCtTypeForResearchResearchProcessor.Result.SessionExpired.toSingle()

          else ->
            ConfirmCtTypeForResearchResearchProcessor.Result.Error(CREATE_MARK_FAILED).toSingle()
        }
      }
      .subscribeOn(computationScheduler)

}