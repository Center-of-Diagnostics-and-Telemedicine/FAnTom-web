package research.tools.grid

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.GridView
import view.GridView.Event
import view.GridView.Model


class GridViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), GridView {

  override fun render(model: Model) {
    updateState(model)
  }


}
