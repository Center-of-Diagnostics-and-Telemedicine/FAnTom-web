package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.INITIAL_GAMMA
import store.BrightnessStore.*

abstract class BrightnessStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): BrightnessStore =
    object : BrightnessStore, Store<Intent, State, Label> by storeFactory.create(
      name = "BrightnessStore",
      initialState = initialState(),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    data class BlackValueChanged(val value: Int) : Result()
    data class WhiteValueChanged(val value: Int) : Result()
    data class GammaValueChanged(val value: Double) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.BlackValueChanged -> copy(blackValue = result.value)
        is Result.WhiteValueChanged -> copy(whiteValue = result.value)
        is Result.GammaValueChanged -> copy(gammaValue = result.value)
      }
  }

  private fun initialState(): State = State(
    blackValue = -1150,
    whiteValue = 350,
    gammaValue = INITIAL_GAMMA
  )
}
