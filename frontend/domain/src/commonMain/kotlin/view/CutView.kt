package view

import com.arkivanov.mvikotlin.core.view.MviView
import view.CutView.Model

interface CutView : MviView<Model, Nothing> {

  data class Model(
    val slice: String,
    val sliceNumber: Int
  )
}

fun initialCutModel(): Model = Model(
  slice = "",
  sliceNumber = 1
)