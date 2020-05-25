package research.tools

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.ToolsView
import view.ToolsView.Event
import view.ToolsView.Model


class ToolsViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), ToolsView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
