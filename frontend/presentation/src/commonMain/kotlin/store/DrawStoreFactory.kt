package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import model.Cut
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
        is Intent.StartMouseMove -> handleStartMove(intent.startDicomX, intent.startDicomY)
        is Intent.Move -> handleMove(intent.dicomX, intent.dicomY, getState)

        is Intent.MouseUp -> handleMouseUp(getState)
        is Intent.MouseOut -> handleMouseOut()
        is Intent.MouseClick -> publish(
          Label.OnClick(
            dicomX = intent.dicomX,
            dicomY = intent.dicomY,
            altKey = intent.altKey
          )
        )
        is Intent.MouseWheel -> publish(Label.ChangeSlice(intent.deltaDicomY))
      }.let {}
    }

    private fun handleStartMove(startDicomX: Double, startDicomY: Double) {
      dispatch(Result.StartMove(startDicomX = startDicomX, startDicomY = startDicomY))
      publish(Label.StartMove(startDicomX = startDicomX, startDicomY = startDicomY))
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
          publish(Label.Drawn(circle = state.circle()))
        }
      }
    }

    private fun handleMove(dicomX: Double, dicomY: Double, getState: () -> State) {
      val state = getState()
      when {
        state.isDrawing -> {
          dispatch(Result.Drawing(dicomX, dicomY))
        }
        state.isContrastBrightness -> {
          dispatch(Result.ContrastBrightness(dicomX, dicomY))
          publish(
            Label.ChangeContrastBrightness(
              deltaX = dicomX - state.startDicomX,
              deltaY = dicomY - state.startDicomY
            )
          )
        }
        else -> {
          dispatch(Result.MouseMove(dicomX, dicomY))
          publish(Label.MouseMove(dicomX, dicomY))
        }
      }
    }

  }

}
