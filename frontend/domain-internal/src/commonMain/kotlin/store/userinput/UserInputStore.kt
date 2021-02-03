package store.userinput

import com.arkivanov.mvikotlin.core.store.Store
import store.userinput.UserInputStore.*

interface UserInputStore : Store<Intent, State, Label> {

  data class State(
    val startDicomX: Double,
    val startDicomY: Double,
    val isContrastBrightness: Boolean = false,
  )

  sealed class Intent {
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Intent()
    data class MouseWheel(val deltaDicomY: Int) : Intent()
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Intent()
    data class MouseUp(val dicomY: Double, val dicomX: Double) : Intent()

    object MouseOut : Intent()
    object DoubleClick : Intent()
  }

  sealed class Label {
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Label()
    object StopMove : Label()
    data class ChangeSlice(val deltaDicomY: Int) : Label()
    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Label()
    object OpenFullCut : Label()
    object ContrastBrightnessChanged : Label()
  }
}