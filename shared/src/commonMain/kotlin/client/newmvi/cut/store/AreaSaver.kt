package client.newmvi.cut.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.single.Single
import model.AreaToSave
import model.SelectedArea

interface AreaSaver {

  @EventsOnAnyScheduler
  fun save(
    selectedAreaWithoutId: AreaToSave,
    researchId: Int
  ): Single<Result>

  sealed class Result {
    class Success(val areaWithId: SelectedArea) : Result()
    data class Error(val message: String) : Result()
  }
}