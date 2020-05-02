package client.newmvi.cut.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.single.Single

interface AreaDeleter {

  @EventsOnAnyScheduler
  fun delete(
    areaId: Int
  ): Single<Result>

  sealed class Result {
    class Success(val deletedAreaId: Int) : Result()
    data class Error(val message: String) : Result()
  }
}