package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
import model.isPlanar
import store.draw.DrawStore.*
import store.draw.DrawStoreAbstractFactory

internal class DrawStoreFactory(
  storeFactory: StoreFactory,
  val cut: Cut,
  val researchId: Int
) : DrawStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  researchId = researchId
) {
  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.StartDraw -> handleStartDraw(intent.startDicomX, intent.startDicomY)
        is Intent.StartContrastBrightness ->
          handleStartContrastBrightness(intent.startDicomX, intent.startDicomY)
        is Intent.StartMouseClick -> handleStartClick(intent.startDicomX, intent.startDicomY)
        is Intent.Move -> handleMove(intent.dicomX, intent.dicomY, getState)

        is Intent.MouseUp -> handleMouseUp(getState)
        is Intent.MouseOut -> handleMouseOut()
        is Intent.CenterMarkClick ->
          publish(Label.CenterMarkClick(intent.startDicomX, intent.startDicomY))
        is Intent.MouseWheel -> publish(Label.ChangeSlice(intent.deltaDicomY))
        Intent.DoubleClick -> publish(Label.OpenFullCut)
      }.let {}
    }

    private fun handleStartClick(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartClick(startDicomX = startDicomX, startDicomY = startDicomY))
      publish(Label.StartClick(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    private fun handleStartContrastBrightness(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartContrastBrightness(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    fun handleStartDraw(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDraw(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    private fun handleMouseOut() {
      dispatch(Result.Idle)
      publish(Label.MouseMove(-1.0, -1.0))
    }

    private fun handleMouseUp(getState: () -> State) {
      val state = getState()
      when {
        state.isDrawing -> {
          dispatch(Result.Idle)
          val circle = state.circle(cut.isPlanar())
          println(circle.toString())
          publish(Label.Drawn(circle = circle))
        }
        state.isContrastBrightness -> {
          dispatch(Result.Idle)
          publish(Label.ContrastBrightnessChanged)
        }
        state.isMoving -> {
          dispatch(Result.Idle)
          publish(Label.StopMove)
        }
      }
    }

    private fun handleMove(dicomX: Double, dicomY: Double, getState: () -> State) {
      val state = getState()
      when {
        state.isDrawing -> {
          if (cut.isPlanar()) {
            dispatch(Result.PlanarDrawing(dicomX, dicomY))
          } else {
            dispatch(Result.MultiPlanarDrawing(dicomX, dicomY))
          }
        }
        state.isContrastBrightness -> {
          dispatch(Result.ContrastBrightness(dicomX, dicomY))
          val deltaX = dicomX - state.startDicomX
          val deltaY = dicomY - state.startDicomY
          publish(Label.ChangeContrastBrightness(deltaX = deltaX, deltaY = deltaY))
        }
        state.isMoving -> {
          dispatch(Result.MouseMoveInClick(dicomX, dicomY))
          val deltaX = dicomX - state.startDicomX
          val deltaY = dicomY - state.startDicomY
          publish(Label.MoveInClick(deltaX = deltaX, deltaY = deltaY))

        }
        else -> {
          dispatch(Result.MouseMove(dicomX, dicomY))
          publish(Label.MouseMove(dicomX, dicomY))
        }
      }
    }

  }

}
