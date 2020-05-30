package research.cut

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.SliderView
import view.SliderView.Event
import view.SliderView.Model

class SliderViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), SliderView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
