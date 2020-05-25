package view

import com.arkivanov.mvikotlin.core.view.MviView
import view.BrightnessView.Event
import view.BrightnessView.Model

interface BrightnessView : MviView<Model, Event> {

  data class Model(
    val blackValue: Int,
    val whiteValue: Int,
    val gammaValue: Double
  )

  sealed class Event {
    data class BlackChanged(val value: Int) : Event()
    data class WhiteChanged(val value: Int) : Event()
    data class GammaChanged(val value: Double) : Event()
  }

}

fun initialBrightnessModel(): Model = Model(
  blackValue = -1150,
  whiteValue = 350,
  gammaValue = 1.0
)
