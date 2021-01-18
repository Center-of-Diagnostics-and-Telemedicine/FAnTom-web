package research.marks

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.MarksView
import view.MarksView.Event
import view.MarksView.Model

class MarksViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), MarksView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
