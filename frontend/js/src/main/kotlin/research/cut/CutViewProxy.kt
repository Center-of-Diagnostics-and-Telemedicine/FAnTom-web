package research.cut

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.CutView
import view.CutView.Event
import view.CutView.Model

class CutViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), CutView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }

}
