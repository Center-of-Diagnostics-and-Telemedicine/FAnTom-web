package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.HasIntValue
import model.Mip
import store.MipStore.*

abstract class MipStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): MipStore =
    object : MipStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MipStore",
      initialState = getInitialState(),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    data class Loaded(val list: List<Mip>, val current: Mip = Mip.No) : Result()
    data class MipMethodChanged(val mip: Mip) : Result()
    data class MipValueChanged(val value: Int) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loaded -> copy(list = result.list, current = result.current)
        is Result.MipMethodChanged -> copy(
          current = result.mip,
          currentValue = if (result.mip is HasIntValue) result.mip.value else null
        )
        is Result.MipValueChanged -> copy(currentValue = result.value)
      }
  }

  private fun getInitialState(): State = State(
    list = listOf(Mip.No, Mip.Average(), Mip.Max()),
    current = Mip.No,
    currentValue = null
  )
}
