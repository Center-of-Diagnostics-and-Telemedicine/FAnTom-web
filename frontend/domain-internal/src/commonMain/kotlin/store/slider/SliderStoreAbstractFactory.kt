package store.slider

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Plane
import model.Research
import store.slider.SliderStore.Intent
import store.slider.SliderStore.State

abstract class SliderStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Plane,
  private val research: Research
) {

  val initialState: State = State(
    currentValue = cut.data!!.nImages / 2,
    maxValue = cut.data!!.nImages,
    defaultValue = 1
  )

  fun create(): SliderStore =
    object : SliderStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "SliderStoreType${cut.type.intType}Id${research.id}",
      initialState = initialState,
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Nothing>

  protected sealed class Result : JvmSerializable {
    data class ValueChanged(val value: Int) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.ValueChanged -> copy(currentValue = result.value)
      }
  }
}
