package client.newmvi.shapes.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.*
import client.domain.repository.ResearchRepository

interface HounsfieldDataLoader {
  @EventsOnAnyScheduler
  fun load(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): Single<Result>

  sealed class Result {
    data class Success(val huValue: Double) : Result()
    data class Error(val message: String) : Result()
  }
}

class HounsfieldDataLoaderImpl(
  private val repository: ResearchRepository
) : HounsfieldDataLoader {

  override fun load(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Single<HounsfieldDataLoader.Result> {
    return singleFromCoroutine {
      repository.getHounsfieldData(axialCoord, frontalCoord, sagittalCoord)
    }
      .map { HounsfieldDataLoader.Result.Success(it) }
      .onErrorReturnValue(HounsfieldDataLoader.Result.Error("Произошла ошибка при попытке получить значение Хаунсфилда"))
  }

}