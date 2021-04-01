package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
import model.DOSE_REPORT_RESEARCH_CATEGORY
import model.Research
import model.isPlanar
import store.draw.DrawStore.*
import store.draw.DrawStoreAbstractFactory

internal class DrawStoreFactory(
  storeFactory: StoreFactory,
  val cut: Cut,
  val research: Research
) : DrawStoreAbstractFactory(
  storeFactory = storeFactory,
  cut = cut,
  research = research
) {
  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> = ExecutorImpl()

  private inner class ExecutorImpl :
    ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.StartDrawEllipse -> {
          if (research.category != DOSE_REPORT_RESEARCH_CATEGORY) {
            handleStartDrawEllipse(intent.startDicomX, intent.startDicomY)
          } else null
        }
        is Intent.StartDrawRectangle -> handleStartDrawRectangle(
          intent.startDicomX,
          intent.startDicomY
        )
        is Intent.StartContrastBrightness ->
          handleStartContrastBrightness(intent.startDicomX, intent.startDicomY)
        is Intent.StartMouseClick -> handleStartClick(intent.startDicomX, intent.startDicomY)
        is Intent.Move -> handleMove(intent.dicomX, intent.dicomY, getState)

        is Intent.MouseUp -> handleMouseUp(getState)
        is Intent.MouseOut -> handleMouseOut()
        is Intent.MouseWheel -> handleMouseData(intent)
        Intent.DoubleClick -> publish(Label.OpenFullCut)
      }.let {}
    }

    private fun handleMouseData(intent: Intent.MouseWheel) =
      if (cut.data.n_images > 1) {
        publish(Label.ChangeSlice(intent.deltaDicomY))
      } else null

    private fun handleStartClick(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartClick(startDicomX = startDicomX, startDicomY = startDicomY))
      publish(Label.StartClick(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    private fun handleStartContrastBrightness(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartContrastBrightness(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    fun handleStartDrawEllipse(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDrawEllipse(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    fun handleStartDrawRectangle(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartDrawRectangle(startDicomX = startDicomX, startDicomY = startDicomY))
    }

    private fun handleMouseOut() {
      dispatch(Result.Idle)
      publish(Label.MouseMove(-1.0, -1.0))
    }

    private fun handleMouseUp(getState: () -> State) {
      val state = getState()
      when {
        state.isDrawingEllipse -> {
          dispatch(Result.Idle)
          val circle = state.circle(cut.isPlanar())
          publish(Label.CircleDrawn(circle = circle))
        }
        state.isDrawingRectangle -> {
          dispatch(Result.Idle)
          val rectangle = state.rectangle()
          publish(Label.RectangleDrawn(rectangle = rectangle))
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
        state.isDrawingEllipse -> {
          if (cut.isPlanar()) {
            dispatch(Result.PlanarDrawing(dicomX, dicomY))
          } else {
            dispatch(Result.MultiPlanarDrawing(dicomX, dicomY))
          }
        }
        state.isDrawingRectangle -> {
          dispatch(Result.PlanarDrawing(dicomX, dicomY))
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
