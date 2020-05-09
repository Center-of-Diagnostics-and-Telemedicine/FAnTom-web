package client.newmvi.cut.store

import client.ResearchApiExceptions
import client.domain.repository.ResearchRepository
import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.single.*
import model.GET_SLICE_FAILED
import model.SliceData

interface CutLoader {

  @EventsOnAnyScheduler
  fun load(
    sliceData: SliceData
  ): Single<Result>

  sealed class Result {
    data class Error(val message: String) : Result()
    class Success(val img: String) : Result()
    object SessionExpired : Result()
  }
}

class CutLoaderImpl(
  private val repository: ResearchRepository
) : CutLoader {

  override fun load(sliceData: SliceData): Single<CutLoader.Result> =
    singleFromCoroutine {
      repository.getSlice(
        black = sliceData.black,
        white = sliceData.white,
        gamma = sliceData.gamma,
        researchId = sliceData.researchId,
        type = sliceData.cutType,
        mipMethod = sliceData.mipMethod,
        slyceNumber = sliceData.sliceNumber,
        aproxSize = sliceData.mipValue
      )
    }
      .map(CutLoader.Result::Success)
      .onErrorResumeNext {
        when (it) {
          is ResearchApiExceptions.ResearchListFetchException -> CutLoader.Result.Error(it.error).toSingle()
          is ResearchApiExceptions.ResearchNotFoundException ->
            CutLoader.Result.Error(it.error).toSingle()

          is ResearchApiExceptions.SessionExpiredException -> CutLoader.Result.SessionExpired.toSingle()
          else -> CutLoader.Result.Error(GET_SLICE_FAILED).toSingle()
        }
      }
      .subscribeOn(computationScheduler)
}