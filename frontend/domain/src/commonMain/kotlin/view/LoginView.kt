package view

import com.arkivanov.mvikotlin.core.view.MviView
import view.LoginView.Event
import view.LoginView.Model

interface LoginView : MviView<Model, Event> {

  data class Model(
      val login: String,
      val password: String,
      val error: String,
      val loading: Boolean
  )

  sealed class Event {
    object Auth : Event()
    data class PasswordChanged(val password: String) : Event()
    data class LoginChanged(val login: String) : Event()

    object DismissError : Event()
  }
}

fun initialModel(): Model = Model(
  login = "",
  password = "",
  error = "",
  loading = false
)
