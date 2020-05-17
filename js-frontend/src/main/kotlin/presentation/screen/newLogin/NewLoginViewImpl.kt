package presentation.screen.newLogin

import client.newmvi.newlogin.view.NewLoginView
import client.newmvi.newlogin.view.NewLoginView.Event
import client.newmvi.newlogin.view.NewLoginView.Model
import com.arkivanov.mvikotlin.core.view.BaseMviView
import debugLog

class NewLoginViewImpl(
  private val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), NewLoginView {

  override fun render(model: Model) {
    debugLog("updateState")
    updateState(model)
  }

  fun dispatchEvent(event: Event) {
    debugLog("dispatch event")
    super.dispatch(event)
  }
}
