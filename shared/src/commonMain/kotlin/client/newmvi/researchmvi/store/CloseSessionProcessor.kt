package client.newmvi.researchmvi.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturnValue
import client.domain.repository.ResearchRepository

interface CloseSessionProcessor {

  @EventsOnAnyScheduler
  fun close(researchId: Int): Single<Result>

  sealed class Result {
    object Success : Result()
    data class Error(val message: String) : Result()
  }
}

class CloseSessionProcessorImpl(
  val repository: ResearchRepository
) : CloseSessionProcessor {

  override fun close(researchId: Int): Single<CloseSessionProcessor.Result> =
    singleFromCoroutine {
      repository.closeSession()
    }
      .map { CloseSessionProcessor.Result.Success }
      .onErrorReturnValue(CloseSessionProcessor.Result.Error("Произошла ошибка"))

}