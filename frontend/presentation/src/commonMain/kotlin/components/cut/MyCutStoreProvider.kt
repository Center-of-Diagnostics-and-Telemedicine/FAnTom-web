package components.cut

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
import store.cut.MyCutStore
import store.cut.MyCutStore.*

class MyCutStoreProvider(
  private val storeFactory: StoreFactory,
  private val brightnessRepository: MyBrightnessRepository,
  private val mipRepository: MyMipRepository,
  private val researchId: Int,
) {

  val initialState: State = State(
    sliceNumber = 1,
    slice = "",
    black = INITIAL_BLACK.toInt(),
    white = INITIAL_WHITE.toInt(),
    gamma = INITIAL_GAMMA,
    mipMethod = Mip.No,
    mipValue = INITIAL_MIP_VALUE,
    mainLoading = false,
    secondaryLoading = false,
    error = ""
  )

  fun provide(): MyCutStore =
    object : MyCutStore, Store<Intent, State, Label> by storeFactory.create(
      name = "MyCutStore",
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
    object MainLoading : Result()
    object SecondaryLoading : Result()
    object DismissError : Result()

    data class Loaded(val slice: String) : Result()
    data class Error(val message: String) : Result()

    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    data class BlackChanged(val blackValue: Int) : Result()
    data class WhiteChanged(val whiteValue: Int) : Result()
    data class GammaChanged(val gammaValue: Double) : Result()
    data class MipChanged(val mip: Mip) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      brightnessRepository
        .black
        .map(Result::BlackChanged)
        .subscribeScoped(onNext = ::dispatch)

      brightnessRepository
        .white
        .map(Result::WhiteChanged)
        .subscribeScoped(onNext = ::dispatch)

      brightnessRepository
        .gamma
        .map(Result::GammaChanged)
        .subscribeScoped(onNext = ::dispatch)

      mipRepository
        .mipMethod
        .map(Result::MipChanged)
        .subscribeScoped(onNext = ::dispatch)

    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleSliceNumberChange -> {
          val sliceNumber = intent.sliceNumber
//          if (inBounds(sliceNumber)) {
          changeSliceNumber(sliceNumber, getState)
//          } else null
        }
        is Intent.HandleBlackChanged -> {
          dispatch(Result.BlackChanged(blackValue = intent.blackValue))
          load(getState, Result.SecondaryLoading, black = intent.blackValue)
        }
        is Intent.HandleWhiteChanged -> {
          dispatch(Result.WhiteChanged(whiteValue = intent.whiteValue))
          load(getState, Result.SecondaryLoading, white = intent.whiteValue)
        }
        is Intent.HandleGammaChanged -> {
          dispatch(Result.GammaChanged(gammaValue = intent.gammaValue))
          load(getState, Result.SecondaryLoading, gamma = intent.gammaValue)
        }
        is Intent.HandleMipChanged -> {
          dispatch(Result.MipChanged(mip = intent.mip))
          load(getState, Result.SecondaryLoading, mipMethod = intent.mip.intValue)
        }
      }.let {}
    }

    private fun changeSliceNumber(sliceNumber: Int, getState: () -> State) {
      val resultSliceNumber = when {
        sliceNumber < 1 -> 1
//        sliceNumber > cut.data.n_images -> cut.data.n_images
        else -> sliceNumber
      }

      if (sliceNumber != getState().sliceNumber) {
        dispatch(Result.SliceNumberChanged(sliceNumber = resultSliceNumber))
        load(getState, Result.SecondaryLoading, sliceNumber = resultSliceNumber)
      }
    }

    private fun load(
      getState: () -> State,
      loadingType: Result,
      mainLoading: Boolean? = getState().mainLoading,
      secondaryLoading: Boolean? = getState().secondaryLoading,
      black: Int? = getState().black,
      white: Int? = getState().white,
      gamma: Double? = getState().gamma,
      mipMethod: Int? = getState().mipMethod.intValue,
      aproxSize: Int? = getState().mipValue,
      sliceNumber: Int? = getState().sliceNumber
    ) {
      if (!mainLoading!! && !secondaryLoading!!) {
        dispatch(loadingType)
//        singleFromCoroutine {
//          repository.getSlice(
//            researchId = research.id,
//            type = cut.type.intType,
//            black = black!!,
//            white = white!!,
//            gamma = gamma!!,
//            mipMethod = mipMethod!!,
//            sliceNumber = getSliceNumber(sliceNumber),
//            aproxSize = aproxSize!!,
//            width = cut.data.screen_size_h,
//            height = 0
//          )
//        }
//          .subscribeOn(ioScheduler)
//          .map(Result::Loaded)
//          .observeOn(mainScheduler)
//          .subscribeScoped(
//            isThreadLocal = true,
//            onSuccess = ::dispatch,
//            onError = ::handleError
//          )
      }
    }

    private fun handleError(error: Throwable) {
      val result = when (error) {
        is ResearchApiExceptions -> Result.Error(error.error)
        else -> {
          println("cut: other exception ${error.message}")
          Result.Error(BASE_ERROR)
        }
      }
      dispatch(result)
    }

//    private fun inBounds(sliceNumber: Int) = sliceNumber <= cut.data.n_images && sliceNumber > 0
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.MainLoading -> copy(mainLoading = true)
        is Result.SecondaryLoading -> copy(secondaryLoading = true)
        is Result.Loaded -> copy(
          mainLoading = false,
          secondaryLoading = false,
          error = "",
          slice = result.slice
        )
        is Result.Error -> copy(
          error = result.message,
          secondaryLoading = false,
          mainLoading = false
        )
        is Result.DismissError -> copy(error = "")

        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.BlackChanged -> copy(black = result.blackValue)
        is Result.WhiteChanged -> copy(white = result.whiteValue)
        is Result.GammaChanged -> copy(gamma = result.gammaValue)
        is Result.MipChanged -> copy(mipMethod = result.mip)
      }
  }
}