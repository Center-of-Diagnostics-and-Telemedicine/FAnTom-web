package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Mip
import view.MipView.Event
import view.MipView.Model

interface MipView : MviView<Model, Event> {

  data class Model(
      val items: List<Mip>,
      val current: Mip,
      val currentValue: Int? = null
  )

  sealed class Event {
    data class ItemClick(val mip: Mip) : Event()
    data class MipValueChanged(val value: Int) : Event()
  }
}

fun initialMipModel(): Model = Model(
  items = listOf(),
  current = Mip.No
)
