package client.newmvi.researchlist.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturnValue
import client.domain.repository.ResearchRepository
import model.Research

interface ResearchListLoader {

  @EventsOnAnyScheduler
  fun load(): Single<Result>

  sealed class Result {
    data class Success(val data: List<Research>) : Result()
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
      .onErrorReturnValue(ResearchListLoader.Result.Error("Произошла ошибка"))

}