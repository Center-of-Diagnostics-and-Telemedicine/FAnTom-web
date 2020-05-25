package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Presets
import store.PresetStore.*


abstract class PresetStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): PresetStore =
    object : PresetStore, Store<Intent, State, Label> by storeFactory.create(
      name = "PresetStore",
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
    data class PresetChanged(val preset: Presets) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.PresetChanged -> copy(current = result.preset)
      }
  }

  private fun getInitialState(): State = State(
    list = listOf(
      Presets.SoftTissue,
      Presets.Vessels,
      Presets.Bones,
      Presets.Brain,
      Presets.Lungs,
    ),
    current = Presets.getInitialPreset()
  )
}
