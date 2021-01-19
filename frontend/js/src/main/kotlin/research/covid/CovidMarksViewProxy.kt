package research.covid

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.CovidMarksView

class CovidMarksViewProxy(
  val updateState: (CovidMarksView.Model) -> Unit
) : BaseMviView<CovidMarksView.Model, CovidMarksView.Event>(), CovidMarksView {

  override fun render(model: CovidMarksView.Model) {
    updateState(model)
  }
}