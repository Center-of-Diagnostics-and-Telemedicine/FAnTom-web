package client.newmvi.login.binder

import client.newmvi.login.store.LoginStore
import client.newmvi.login.view.LoginView


internal object LoginViewEventToIntentMapper {

  operator fun invoke(event: LoginView.Event): LoginStore.Intent =
    when (event) {
      is LoginView.Event.ErrorShown -> LoginStore.Intent.DismissError
      is LoginView.Event.Init -> LoginStore.Intent.Init
      is LoginView.Event.Auth -> LoginStore.Intent.Auth(event.login, event.password)
    }
}