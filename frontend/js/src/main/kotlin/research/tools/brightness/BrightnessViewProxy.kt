package research.tools.brightness

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.BrightnessView
import view.BrightnessView.Event
import view.BrightnessView.Model

class BrightnessViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), BrightnessView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
