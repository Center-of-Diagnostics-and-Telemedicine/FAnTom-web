package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
import store.cut.SliderStore.*
import store.cut.SliderStoreAbstractFactory

internal class SliderStoreFactory(
  storeFactory: StoreFactory,
  cut: Cut,
  researchId: Int
) : SliderStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  researchId = researchId
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleChange -> {
          dispatch(Result.ValueChanged(intent.value))
          publish(Label.SliceNumberChanged(intent.value))
        }
      }.let {}
    }
  }

}
