package research.cut.shapes

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.ShapesView
import view.ShapesView.Event
import view.ShapesView.Model

class ShapesViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), ShapesView {

  override fun render(model: Model) {
    updateState(model)
  }


}
