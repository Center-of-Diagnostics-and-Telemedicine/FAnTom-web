package store.cut

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Cut
import store.cut.SliderStore.Intent
import store.cut.SliderStore.State

abstract class SliderStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Cut,
  private val researchId: Int
) {

  val initialState: State = State(
    currentValue = cut.data!!.maxFramesSize / 2,
    maxValue = cut.data!!.maxFramesSize,
    defaultValue = 1
  )

  fun create(): SliderStore =
    object : SliderStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "SliderStoreType${cut.type.intType}Id${researchId}",
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
