package client.newmvi.shapes.store

import client.ResearchApiExceptions
import client.domain.repository.ResearchRepository
import client.newmvi.researchmvi.store.ResearchDataLoader
import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.*
import model.HOUNSFIELD_FETCH_ERROR

interface HounsfieldDataLoader {
  @EventsOnAnyScheduler
  fun load(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): Single<Result>

  sealed class Result {
    data class Success(val huValue: Double) : Result()
    data class Error(val message: String) : Result()
    data class AxialValueError(val message: String) : Result()
    data class FrontalValueError(val message: String) : Result()
    data class SagittalValueError(val message: String) : Result()
    object SessionExpired : Result()
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
      .onErrorResumeNext {
        when (it) {
          is ResearchApiExceptions.HounsfieldFetchError ->
            HounsfieldDataLoader.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.IncorrectAxialValueException ->
            HounsfieldDataLoader.Result.AxialValueError(it.error).toSingle()

          is ResearchApiExceptions.IncorrectFrontalValueException ->
            HounsfieldDataLoader.Result.FrontalValueError(it.error).toSingle()

          is ResearchApiExceptions.IncorrectSagittalValueException ->
            HounsfieldDataLoader.Result.SagittalValueError(it.error).toSingle()

          is ResearchApiExceptions.ResearchNotFoundException ->
            HounsfieldDataLoader.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.SessionExpiredException ->
            HounsfieldDataLoader.Result.SessionExpired.toSingle()

          else -> HounsfieldDataLoader.Result.Error(HOUNSFIELD_FETCH_ERROR).toSingle()
        }
      }
//      .onErrorReturnValue()
  }

}