package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
import repository.ResearchRepository
import store.cut.ShapesStore.Intent
import store.cut.ShapesStore.State
import store.cut.ShapesStoreAbstractFactory

internal class ShapesStoreFactory(
  storeFactory: StoreFactory,
  val cut: Cut,
  val repository: ResearchRepository,
  val researchId: Int
) : ShapesStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  researchId = researchId
) {
  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Nothing>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleSliceNumberChange -> dispatch(Result.SliceNumberChanged(intent.sliceNumber))
        is Intent.HandleHorizontalLineChanged -> dispatch(Result.HorizontalLineChanged(intent.line))
        is Intent.HandleVerticalLineChanged -> dispatch(Result.VerticalLineChanged(intent.line))
      }.let {}
    }
  }

}
