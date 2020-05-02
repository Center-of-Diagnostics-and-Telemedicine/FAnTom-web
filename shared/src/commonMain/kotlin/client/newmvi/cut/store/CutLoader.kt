package client.newmvi.cut.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.single.Single
import model.SliceData

interface CutLoader {

  @EventsOnAnyScheduler
  fun load(
    sliceData: SliceData
  ): Single<Result>

  sealed class Result {
    class Success(val url: String) : Result()
    data class Error(val message: String) : Result()
  }
}