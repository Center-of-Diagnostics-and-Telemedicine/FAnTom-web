package store.tools

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Presets
import model.ResearchData
import model.initialPreset
import model.initialPresets
import store.tools.PresetStore.*


abstract class PresetStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val data: ResearchData
) {

  fun create(): PresetStore =
    object : PresetStore, Store<Intent, State, Label> by storeFactory.create(
      name = "PresetStore",
      initialState = getInitialState(),
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Unit, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    data class PresetChanged(val preset: Presets) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.PresetChanged -> copy(current = result.preset)
      }
  }

  private fun getInitialState(): State =
    State(
      list = initialPresets(data.type),
      current = initialPreset(data.type)
    )
}

