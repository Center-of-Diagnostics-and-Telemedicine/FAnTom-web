package client.newmvi.researchlist.store

import client.ResearchApiExceptions
import client.domain.repository.ResearchRepository
import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.*
import model.Research

interface ResearchListLoader {

  @EventsOnAnyScheduler
  fun load(): Single<Result>

  sealed class Result {
    data class Success(val data: List<Research>) : Result()
    object SessionExpired : Result()
    data class Error(val message: String) : Result()
  }
}

class ResearchListLoaderImpl(
  private val repository: ResearchRepository
) : ResearchListLoader {

  override fun load(): Single<ResearchListLoader.Result> =
    singleFromCoroutine {
      repository.getResearches()
    }
      .map { ResearchListLoader.Result.Success(it) }
      .onErrorResumeNext {
        when (it) {
          is ResearchApiExceptions.ResearchListFetchException ->
            ResearchListLoader.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.SessionExpiredException ->
            ResearchListLoader.Result.SessionExpired.toSingle()

          else -> ResearchListLoader.Result.Error("Произошла ошибка").toSingle()
        }
      }
}