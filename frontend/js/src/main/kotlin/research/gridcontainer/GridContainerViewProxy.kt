package research.gridcontainer

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.GridContainerView
import view.GridContainerView.Model

class GridContainerViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Nothing>(), GridContainerView {

  override fun render(model: Model) {
    updateState(model)
  }
}
