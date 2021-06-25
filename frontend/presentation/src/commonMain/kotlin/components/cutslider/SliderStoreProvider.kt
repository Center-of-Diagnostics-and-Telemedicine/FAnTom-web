package components.cutslider

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.CutType
import store.slider.SliderStore
import store.slider.SliderStore.Intent
import store.slider.SliderStore.State

internal class SliderStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val cutType: CutType
) {

  fun provide(): SliderStore =
    object : SliderStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "SliderStore_${researchId}_${cutType.intType}",
      initialState = State(
        currentValue = 1,
        maxValue = 100,
        defaultValue = 1,
      ),
//      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class ValueChanged(val value: Int) : Result()
  }

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Nothing>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleChange -> dispatch(Result.ValueChanged(intent.value))
      }.let {}
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.ValueChanged -> copy(currentValue = result.value)
      }
  }
}