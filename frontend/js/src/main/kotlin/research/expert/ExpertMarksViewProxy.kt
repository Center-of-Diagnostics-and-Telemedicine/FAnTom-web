package research.expert

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.ExpertMarksView

class ExpertMarksViewProxy(
  val updateState: (ExpertMarksView.Model) -> Unit
) : BaseMviView<ExpertMarksView.Model, ExpertMarksView.Event>(), ExpertMarksView {

  override fun render(model: ExpertMarksView.Model) {
    updateState(model)
  }
}