package components.cutcontainer

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import repository.MyBrightnessRepository
import repository.MyMipRepository
import repository.ResearchRepository
import store.cut.CutContainerStore
import store.cut.CutContainerStore.*

class CutContainerStoreProvider(
  private val storeFactory: StoreFactory,
  private val brightnessRepository: MyBrightnessRepository,
  private val researchRepository: ResearchRepository,
  private val mipRepository: MyMipRepository,
  private val researchId: Int,
  private val plane: Plane
) {

  val initialState = State(
    sliceNumber = plane.data.nImages / 2,
    blackValue = INITIAL_BLACK.toInt(),
    whiteValue = INITIAL_WHITE.toInt(),
    gammaValue = INITIAL_GAMMA,
    mip = Mip.initial,
  )

  fun provide(): CutContainerStore =
    object : CutContainerStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CutStore_${researchId}_${plane.type.intType}",
      initialState = initialState,
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private sealed class Result : JvmSerializable {
    data class SliceNumber(val value: Int) : Result()
    data class BlackChanged(val value: Int) : Result()
    data class WhiteChanged(val value: Int) : Result()
    data class GammaChanged(val value: Double) : Result()
    data class MipChanged(val value: Mip) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      println("executeAction")
      brightnessRepository.black
        .map(Result::BlackChanged)
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      brightnessRepository.white
        .map(Result::WhiteChanged)
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      brightnessRepository.gamma
        .map(Result::GammaChanged)
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      mipRepository.mipMethod
        .map(Result::MipChanged)
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeSliceNumber -> Result.SliceNumber(intent.sliceNumber)
        is Intent.ChangeBlackValue -> Result.BlackChanged(intent.value)
        is Intent.ChangeWhiteValue -> Result.WhiteChanged(intent.value)
        is Intent.ChangeGammaValue -> Result.GammaChanged(intent.value)
        is Intent.ChangeMipValue -> Result.MipChanged(intent.mip)
        else -> throw NotImplementedError("Intent not implemented in CutContainerStoreProvider $intent")
      }.let {}
    }

    private fun handleError(error: Throwable) {
      error.printStackTrace()
    }
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumber -> copy(sliceNumber = result.value)
        is Result.BlackChanged -> copy(blackValue = result.value)
        is Result.WhiteChanged -> copy(whiteValue = result.value)
        is Result.GammaChanged -> copy(gammaValue = result.value)
        is Result.MipChanged -> copy(mip = result.value)
      }
  }
}