package login

import com.arkivanov.mvikotlin.core.view.BaseMviView
import view.LoginView
import view.LoginView.Event
import view.LoginView.Model

class LoginViewProxy(
  val updateState: (Model) -> Unit
) : BaseMviView<Model, Event>(), LoginView {

  override fun render(model: Model) {
    updateState(model)
  }

  public override fun dispatch(event: Event) {
    super.dispatch(event)
  }
}
