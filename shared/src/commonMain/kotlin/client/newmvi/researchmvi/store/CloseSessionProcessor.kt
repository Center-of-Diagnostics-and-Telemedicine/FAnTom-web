package client.newmvi.researchmvi.store

import client.ResearchApiExceptions
import client.domain.repository.ResearchRepository
import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.*
import model.HOUNSFIELD_FETCH_ERROR

interface CloseSessionProcessor {

  @EventsOnAnyScheduler
  fun close(researchId: Int): Single<Result>

  sealed class Result {
    object Success : Result()
    data class Error(val message: String) : Result()
    object SessionExpired : Result()
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
      .onErrorResumeNext {
        when (it) {
          is ResearchApiExceptions.CloseSessionException ->
            CloseSessionProcessor.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.ResearchNotFoundException ->
            CloseSessionProcessor.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.SessionExpiredException ->
            CloseSessionProcessor.Result.SessionExpired.toSingle()

          else -> CloseSessionProcessor.Result.Error(HOUNSFIELD_FETCH_ERROR).toSingle()
        }
      }

}