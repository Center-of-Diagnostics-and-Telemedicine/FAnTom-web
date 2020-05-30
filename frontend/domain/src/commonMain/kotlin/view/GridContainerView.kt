package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.Cut
import model.Grid
import model.initialFourGrid
import view.GridContainerView.Model

interface GridContainerView : MviView<Model, Nothing> {

  data class Model(
    val items: List<Cut>,
    val grid: Grid
  )
}

fun initialGridContainerModel(): Model = Model(
  items = listOf(),
  grid = initialFourGrid()
)
