package client.newmvi.researchmvi.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.single.Single
import model.ResearchSlicesSizesData

interface ResearchDataLoader {

  @EventsOnAnyScheduler
  fun load(researchId: Int): Single<Result>

  sealed class Result {
    class Success(val researchData: ResearchSlicesSizesData) : Result()
    data class Error(val message: String) : Result()
  }
}