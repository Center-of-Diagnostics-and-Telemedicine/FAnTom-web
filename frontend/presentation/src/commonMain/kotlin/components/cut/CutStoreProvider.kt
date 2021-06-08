package components.cut

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import store.cut.CutStore
import store.cut.CutStore.*

internal class CutStoreProvider(
  private val storeFactory: StoreFactory,
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

  fun provide(): CutStore =
    object : CutStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CutStore",
      initialState = initialState,
//      bootstrapper = SimpleBootstrapper(Unit),
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
    data class MipValueChanged(val mipValue: Int) : Result()
    data class PresetChanged(val black: Int, val white: Int) : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) =
      load(getState = getState, loadingType = Result.MainLoading)

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
        is Intent.HandleMipValueChanged -> {
          dispatch(Result.MipValueChanged(mipValue = intent.mipValue))
          load(getState, Result.SecondaryLoading, aproxSize = intent.mipValue)
        }
        is Intent.HandlePresetChanged -> {
          dispatch(Result.PresetChanged(black = intent.presets.black, white = intent.presets.white))
          load(
            getState,
            Result.SecondaryLoading,
            black = intent.presets.black,
            white = intent.presets.white
          )
        }
        is Intent.HandleCircleDrawn -> {
//          publish(Label.CircleDrawn(intent.circle, getState().sliceNumber, cut))
        }
        is Intent.HandleRectangleDrawn -> {
//          publish(Label.RectangleDrawn(intent.rectangle, getState().sliceNumber, cut))
        }
        is Intent.HandleExternalSliceNumberChanged -> {
          publish(Label.ExternalSliceNumberChanged(intent.externalCut, intent.sliceNumber))
        }
        is Intent.HandleMarks -> {
          publish(Label.Marks(intent.list))
        }
        is Intent.HandleExpertMarks -> {
          publish(Label.ExpertMarks(intent.list))
        }
        is Intent.HandleMarkSelected -> {
          publish(Label.SelectMark(intent.mark))
        }
        is Intent.HandleMarkUnselect -> {
          publish(Label.UnselectMark(intent.mark))
        }
        is Intent.ChangeSliceNumberByMarkCenter -> {
//          cut.getSliceNumberByMark(intent.mark)?.let { changeSliceNumber(it, getState) }
        }
        is Intent.ChangeContrastBrightness -> {
          handleContrastBrightness(intent.deltaX, intent.deltaY, getState)
        }
        Intent.ContrasBrightnessChanged -> {
          val state = getState()
          publish(Label.ContrastBrightnessChanged(state.black, state.white))
        }
        is Intent.ChangeSliceNumberByDraw -> {
          val sliceNumber = getState().sliceNumber + intent.deltaDicomY
//          if (inBounds(sliceNumber)) {
          changeSliceNumber(sliceNumber, getState)
//          } else null
        }
        is Intent.HandleMarkUpdateWithoutSave -> {
          publish(Label.UpdateMarkWithoutSave(intent.mark))
        }
        is Intent.HandleStopMoving -> {
          publish(Label.StopMoving)
        }
        is Intent.HandleMarkUpdateWithSave -> {
          publish(Label.UpdateMarkWithSave(intent.mark))
        }
        is Intent.HandleStartClick -> {
          publish(Label.StartClick(intent.startDicomX, intent.startDicomY))
        }
        Intent.OpenFullCut -> {
//          publish(Label.OpenFullCut(cut))
        }

        is Intent.HandleChangeCutType -> {
//          publish(Label.ChangeCutType(intent.cutType, cut))
        }
        Intent.DismissErrorRequested -> dispatch(Result.DismissError)
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
//        publish(Label.SliceNumberChanged(sliceNumber = resultSliceNumber, cut = cut))
      }
    }

    private fun handleContrastBrightness(
      deltaX: Double,
      deltaY: Double,
      state: () -> State
    ) {
      val oldBlack = state().black
      val oldWhite = state().white
      val black = oldBlack - deltaY - deltaX
      val white = oldWhite - deltaY + deltaX
      dispatch(Result.BlackChanged(blackValue = black.toInt()))
      dispatch(Result.WhiteChanged(whiteValue = white.toInt()))
      load(state, Result.SecondaryLoading)
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

    private fun getSliceNumber(sliceNumber: Int?): Int {
      return 0
//      return if (cut.type.isDoseReport()) {
//        when (cut.type) {
//          CutType.CT_0 -> 0
//          CutType.CT_1 -> 1
//          CutType.CT_2 -> 2
//          else -> sliceNumber!!
//        }
//      } else sliceNumber!!
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
        is Result.MipValueChanged -> copy(mipValue = result.mipValue)
        is Result.PresetChanged -> copy(black = result.black, white = result.white)
      }
  }
}