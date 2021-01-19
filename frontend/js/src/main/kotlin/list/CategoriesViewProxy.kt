package list

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.CategoryView
import view.CategoryView.Event
import view.CategoryView.Model

class CategoriesViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), CategoryView {

  override fun render(model: Model) {
    updateState(model)
  }
}