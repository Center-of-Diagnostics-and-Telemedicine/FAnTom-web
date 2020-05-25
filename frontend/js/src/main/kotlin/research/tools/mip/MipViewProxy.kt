package research.tools.mip

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.MipView
import view.MipView.Event
import view.MipView.Model

class MipViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), MipView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
