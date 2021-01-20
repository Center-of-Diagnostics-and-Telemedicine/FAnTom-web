package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
import model.Research
import store.slider.SliderStore.Intent
import store.slider.SliderStore.State
import store.slider.SliderStoreAbstractFactory

internal class SliderStoreFactory(
  storeFactory: StoreFactory,
  cut: Cut,
  research: Research
) : SliderStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  research = research
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
