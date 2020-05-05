package client.newmvi.researchmvi.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturnValue
import client.domain.repository.ResearchRepository
import model.CTType

interface CloseResearchProcessor {

  @EventsOnAnyScheduler
  fun close(ctType: CTType, leftPercent: Int, rightPercent: Int, researchId: Int): Single<Result>

  sealed class Result {
    object Success : Result()
    data class Error(val message: String) : Result()
  }
}

class CloseResearchProcessorImpl(
  val repository: ResearchRepository
) : CloseResearchProcessor {

  override fun close(ctType: CTType, leftPercent: Int, rightPercent: Int, researchId: Int): Single<CloseResearchProcessor.Result> =
    singleFromCoroutine {
      repository.closeResearch(ctType, leftPercent, rightPercent, researchId)
    }
      .map { CloseResearchProcessor.Result.Success }
      .onErrorReturnValue(CloseResearchProcessor.Result.Error("Произошла ошибка"))

}