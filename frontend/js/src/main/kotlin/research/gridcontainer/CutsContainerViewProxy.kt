package research.gridcontainer

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.CutsContainerView
import view.CutsContainerView.Model

class CutsContainerViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Nothing>(), CutsContainerView {

  override fun render(model: Model) {
    updateState(model)
  }
}
