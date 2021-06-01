package components.brightness

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.INITIAL_BLACK
import model.INITIAL_GAMMA
import model.INITIAL_WHITE
import repository.BrightnessRepository
import store.tools.BrightnessStore
import store.tools.BrightnessStore.*

internal class BrightnessStoreProvider(
  private val storeFactory: StoreFactory,
  private val researchId: Int,
  private val brightnessRepository: BrightnessRepository
) {

  fun provide(): BrightnessStore =
    object : BrightnessStore, Store<Intent, State, Label> by storeFactory.create(
      name = "BrightnessStore",
      initialState = State(INITIAL_BLACK.toInt(), INITIAL_WHITE.toInt(), INITIAL_GAMMA),
//      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class BlackValueChanged(val value: Int) : Result()
    data class WhiteValueChanged(val value: Int) : Result()
    data class GammaValueChanged(val value: Double) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleBlackChanged -> handleBlackChanged(intent.value)
        is Intent.HandleWhiteValueChanged -> handleWhiteChanged(intent.value)
        is Intent.HandleGammaValueChanged -> handleGammaChanged(intent.value)
        is Intent.HandleContrastBrightnessChanged -> {
          handleBlackChanged(intent.black)
          handleWhiteChanged(intent.white)
        }
        is Intent.PresetChanged -> {
          dispatch(Result.BlackValueChanged(intent.item.black))
          dispatch(Result.WhiteValueChanged(intent.item.white))
        }
      }.let {}
    }

    private fun handleBlackChanged(value: Int) {
      brightnessRepository.setBlackValue(value)
      dispatch(Result.BlackValueChanged(value))
      publish(Label.BlackChanged(value))
    }

    private fun handleWhiteChanged(value: Int) {
      brightnessRepository.setWhiteValue(value)
      dispatch(Result.WhiteValueChanged(value))
      publish(Label.WhiteChanged(value))
    }

    private fun handleGammaChanged(value: Double) {
      brightnessRepository.setGammaValue(value)
      dispatch(Result.GammaValueChanged(value))
      publish(Label.GammaChanged(value))
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.BlackValueChanged -> copy(blackValue = result.value)
        is Result.WhiteValueChanged -> copy(whiteValue = result.value)
        is Result.GammaValueChanged -> copy(gammaValue = result.value)
      }
  }
}