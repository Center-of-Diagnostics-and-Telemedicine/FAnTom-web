package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import model.Cut
import model.GET_SLICE_FAILED
import model.ResearchApiExceptions
import model.getSliceNumberByMark
import repository.ResearchRepository
import store.cut.CutStore.*
import store.cut.CutStoreAbstractFactory

internal class CutStoreFactory(
  storeFactory: StoreFactory,
  val cut: Cut,
  val repository: ResearchRepository,
  val researchId: Int
) : CutStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  researchId = researchId
) {
  override fun createExecutor(): Executor<Intent, Unit, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) = load(getState)

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleBlackChanged -> {
          dispatch(Result.BlackChanged(blackValue = intent.blackValue))
          load(getState)
        }
        is Intent.HandleSliceNumberChange -> {
          changeSliceNumber(intent.sliceNumber, getState)
        }
        is Intent.HandleWhiteChanged -> {
          dispatch(Result.WhiteChanged(whiteValue = intent.whiteValue))
          load(getState)
        }
        is Intent.HandleGammaChanged -> {
          dispatch(Result.GammaChanged(gammaValue = intent.gammaValue))
          load(getState)
        }
        is Intent.HandleMipChanged -> {
          dispatch(Result.MipChanged(mip = intent.mip))
          load(getState)
        }
        is Intent.HandleMipValueChanged -> {
          dispatch(Result.MipValueChanged(mipValue = intent.mipValue))
          load(getState)
        }
        is Intent.HandlePresetChanged -> {
          dispatch(Result.PresetChanged(black = intent.presets.black, white = intent.presets.white))
          load(getState)
        }
        is Intent.HandleCircleDrawn -> {
          publish(Label.CircleDrawn(intent.circle, getState().sliceNumber, cut))
        }
        is Intent.HandleExternalSliceNumberChanged -> {
          publish(Label.ExternalSliceNumberChanged(intent.externalCut, intent.sliceNumber))
        }
        is Intent.HandleMarks -> {
          publish(Label.Marks(intent.list))
        }
        is Intent.HandleMarkSelected -> {
          publish(Label.SelectMark(intent.mark))
        }
        is Intent.HandleMarkCenter -> {
          publish(Label.CenterMark(intent.mark))
        }
        is Intent.HandleMarkUnselect -> {
          publish(Label.UnselectMark(intent.mark))
        }
        is Intent.ChangeSliceNumberByMarkCenter -> {
          cut.getSliceNumberByMark(intent.mark)?.let { changeSliceNumber(it, getState) }
        }
        is Intent.ChangeContrastBrightness -> {
          handleContrastBrightness(intent.deltaX, intent.deltaY, getState)
        }
        Intent.ContrasBrightnessChanged -> {
          val state = getState()
          publish(Label.ContrastBrightnessChanged(state.black, state.white))
        }
        is Intent.ChangeSliceNumberByDraw -> {
          changeSliceNumber(getState().sliceNumber + intent.deltaDicomY, getState)
        }
        is Intent.HandleCircleUpdate -> publish(Label.CircleUpdate(intent.circleToUpdate, cut))
        is Intent.HandleExternalCircleUpdate -> publish(
          Label.ExternalCircleChanged(intent.circle, intent.cut)
        )
      }.let {}
    }

    private fun changeSliceNumber(sliceNumber: Int, getState: () -> State) {
      dispatch(Result.SliceNumberChanged(sliceNumber = sliceNumber))
      load(getState)
      publish(Label.SliceNumberChanged(sliceNumber = sliceNumber, cut = cut))
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
      load(state)
    }

    private fun load(getState: () -> State) {
      val state = getState()
      if (state.loading.not()) {
        dispatch(Result.Loading)
        singleFromCoroutine {
          repository.getSlice(
            researchId = researchId,
            type = cut.type.intType,
            black = state.black,
            white = state.white,
            gamma = state.gamma,
            mipMethod = state.mipMethod.intValue,
            sliceNumber = state.sliceNumber,
            aproxSize = state.mipValue
          )
        }
          .subscribeOn(ioScheduler)
          .map(Result::Loaded)
          .observeOn(mainScheduler)
          .subscribeScoped(
            isThreadLocal = true,
            onSuccess = ::dispatch,
            onError = ::handleError
          )
      }
    }

    private fun handleError(error: Throwable) {
      val result = when (error) {
        is ResearchApiExceptions.SliceFetchException -> Result.Error(error.error)
        is ResearchApiExceptions.IncorrectSliceNumberException -> Result.Error(error.error)
        is ResearchApiExceptions.ResearchNotFoundException -> Result.Error(error.error)
        else -> {
          println("cut: other exception ${error.message}")
          Result.Error(GET_SLICE_FAILED)
        }
      }
      dispatch(result)
    }
  }

}


