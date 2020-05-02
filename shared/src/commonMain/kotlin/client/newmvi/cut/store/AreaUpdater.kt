package client.newmvi.cut.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.single.Single
import model.SelectedArea

interface AreaUpdater {

  @EventsOnAnyScheduler
  fun update(
    selectedArea: SelectedArea
  ): Single<Result>

  sealed class Result {
    data class Success(val area: SelectedArea) : Result()
    data class Error(val message: String) : Result()
  }
}