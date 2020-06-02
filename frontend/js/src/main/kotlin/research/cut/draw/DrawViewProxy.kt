package research.cut.draw

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.DrawView
import view.DrawView.Event
import view.DrawView.Model

class DrawViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), DrawView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
