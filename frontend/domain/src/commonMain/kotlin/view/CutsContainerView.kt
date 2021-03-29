package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Cut
import model.Grid
import model.ResearchType
import model.initialFourGrid
import view.CutsContainerView.Model

interface CutsContainerView : MviView<Model, Nothing> {

  data class Model(
    val items: List<Cut>,
    val grid: Grid
  )
}

fun initialCutsContainerModel(type: ResearchType, doseReport: Boolean): Model = Model(
  items = listOf(),
  grid = initialFourGrid(type, doseReport)
)
