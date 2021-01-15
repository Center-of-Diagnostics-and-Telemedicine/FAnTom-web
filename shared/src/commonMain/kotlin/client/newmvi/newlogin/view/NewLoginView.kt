package client.newmvi.newlogin.view

import client.newmvi.newlogin.view.NewLoginView.Event
import client.newmvi.newlogin.view.NewLoginView.Model
import com.arkivanov.mvikotlin.core.view.MviView

interface NewLoginView : MviView<Model, Event> {

  data class Model(
    val login: String,
    val password: String,
    val authorized: Boolean,
    val error: String,
    val loading: Boolean
  )

  sealed class Event {
    data class Auth(val login: String, val password: String) : Event()
    data class PasswordChanged(val password: String) : Event()
    data class LoginChanged(val login: String) : Event()

    object DismissError : Event()
  }
}

fun initialModel(): Model = Model("","",false,"",false)
