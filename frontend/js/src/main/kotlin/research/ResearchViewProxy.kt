package research

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.ResearchView
import view.ResearchView.Event
import view.ResearchView.Model

class ResearchViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), ResearchView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
