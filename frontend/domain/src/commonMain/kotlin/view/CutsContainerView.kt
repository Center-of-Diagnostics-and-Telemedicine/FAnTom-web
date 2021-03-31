package view

import com.arkivanov.mvikotlin.core.view.MviView
import model.*
import view.CutsContainerView.Model

interface CutsContainerView : MviView<Model, Nothing> {

  data class Model(
    val items: List<Cut>,
    val grid: Grid
  )
}

fun initialCutsContainerModel(type: ResearchType, doseReport: Boolean, data: ResearchSlicesSizesDataNew): Model = Model(
  items = listOf(),
  grid = initialFourGrid(type, doseReport, data.modalities)
)
