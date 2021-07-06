package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Plane
import model.Research
import store.userinput.UserInputStore.*
import store.userinput.UserInputStoreAbstractFactory

internal class UserInputStoreFactory(
  storeFactory: StoreFactory,
  val cut: Plane,
  val research: Research
) : UserInputStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  research = research
) {
  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.StartContrastBrightness ->
          handleStartContrastBrightness(intent.startDicomX, intent.startDicomY)
        is Intent.MouseWheel -> publish(Label.ChangeSlice(intent.deltaDicomY))
        Intent.MouseOut -> handleMouseOut()
        Intent.DoubleClick -> publish(Label.OpenFullCut)
        is Intent.MouseMove -> handleMove(intent.dicomX, intent.dicomY, getState)
        is Intent.MouseUp -> handleMouseUp(getState)
      }.let { }
    }

    private fun handleStartContrastBrightness(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartContrastBrightness(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    private fun handleMouseOut() {
      dispatch(Result.Idle)
      publish(Label.MouseMove(-1.0, -1.0))
    }

    private fun handleMove(dicomX: Double, dicomY: Double, getState: () -> State) {
      val state = getState()
      when {
        state.isContrastBrightness -> {
          dispatch(Result.ContrastBrightness(dicomX, dicomY))
          val deltaX = dicomX - getState().startDicomX
          val deltaY = dicomY - getState().startDicomY
          publish(Label.ChangeContrastBrightness(deltaX = deltaX, deltaY = deltaY))
        }
        else -> {
          dispatch(Result.MouseMove(dicomX, dicomY))
          publish(Label.MouseMove(dicomX, dicomY))
        }
      }
    }

    private fun handleMouseUp(getState: () -> State) {
      if (getState().isContrastBrightness) {
        dispatch(Result.Idle)
        publish(Label.ContrastBrightnessChanged)
      }
    }
  }
}