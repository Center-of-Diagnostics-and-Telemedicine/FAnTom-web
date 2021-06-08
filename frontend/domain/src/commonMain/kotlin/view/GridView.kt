package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.*
import view.GridView.Event
import view.GridView.Model

interface GridView : MviView<Model, Event> {

  data class Model(
    val items: List<GridModel>,
    val current: GridModel
  )

  sealed class Event {
    data class ItemClick(val gridType: GridType) : Event()
  }
}

fun initialGridModel(type: ResearchType, doseReport: Boolean, data: ResearchData): Model = Model(
  items = listOf(),
  current = initialFourGrid(type, doseReport, data.modalities)
)
