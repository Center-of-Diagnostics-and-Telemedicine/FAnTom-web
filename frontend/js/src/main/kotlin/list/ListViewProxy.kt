package list

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.ListView
import view.ListView.Event
import view.ListView.Model

class ListViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), ListView {

  override fun render(model: Model) {
    updateState(model)
  }


}
