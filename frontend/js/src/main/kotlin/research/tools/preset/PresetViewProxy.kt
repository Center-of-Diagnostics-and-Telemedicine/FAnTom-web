package research.tools.preset

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.PresetView
import view.PresetView.Event
import view.PresetView.Model

class PresetViewProxy(
    val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), PresetView {

  override fun render(model: Model) {
    updateState(model)
  }


}
