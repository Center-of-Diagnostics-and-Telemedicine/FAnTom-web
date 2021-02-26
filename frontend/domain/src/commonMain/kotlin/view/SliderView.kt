package view

import com.arkivanov.mvikotlin.core.view.MviView
import view.SliderView.Event
import view.SliderView.Model

interface SliderView : MviView<Model, Event> {

  data class Model(
    val currentValue: Int,
    val maxValue: Int,
    val defaultValue: Int
  )

  sealed class Event {
    data class HandleOnChange(val value: Int) : Event()
  }
}

fun initialSliderModel(): Model = Model(
  currentValue = 1,
  maxValue = 1,
  defaultValue = 1
)
