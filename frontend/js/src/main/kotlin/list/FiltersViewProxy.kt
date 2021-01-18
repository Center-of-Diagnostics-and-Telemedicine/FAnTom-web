package list

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.FilterView
import view.FilterView.Event
import view.FilterView.Model

class FiltersViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), FilterView {

  override fun render(model: Model) {
    updateState(model)
  }


}
