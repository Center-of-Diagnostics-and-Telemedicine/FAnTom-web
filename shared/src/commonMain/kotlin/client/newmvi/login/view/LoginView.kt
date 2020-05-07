package client.newmvi.login.view

import com.badoo.reaktive.subject.publish.PublishSubject
import client.newmvi.researchmvi.BaseEvent
import client.newmvi.researchmvi.BaseView

interface LoginView : BaseView<LoginView.Event> {

  val events: PublishSubject<Event>

  fun show(model: LoginViewModel)

  data class LoginViewModel(
    val isLoading: Boolean,
    val error: String,
    val authorized: Boolean,
    val loginError: Boolean = false,
    val passwordError: Boolean = false
  )

  sealed class Event : BaseEvent {
    object Init : Event()
    object ErrorShown : Event()
    data class Auth(val login: String, val password: String) : Event()
  }
}