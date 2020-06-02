package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
import store.slider.SliderStore.Intent
import store.slider.SliderStore.State
import store.slider.SliderStoreAbstractFactory

internal class SliderStoreFactory(
  storeFactory: StoreFactory,
  cut: Cut,
  researchId: Int
) : SliderStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  researchId = researchId
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Nothing>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleChange -> dispatch(Result.ValueChanged(intent.value))
      }.let {}
    }
  }

}
