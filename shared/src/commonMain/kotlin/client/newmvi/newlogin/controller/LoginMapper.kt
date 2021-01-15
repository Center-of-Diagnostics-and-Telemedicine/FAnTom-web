package client.newmvi.newlogin.controller

import client.newmvi.newlogin.controller.LoginController.Output
import client.newmvi.newlogin.store.NewLoginStore.Intent
import client.newmvi.newlogin.store.NewLoginStore.State
import client.newmvi.newlogin.view.NewLoginView.Event
import client.newmvi.newlogin.view.NewLoginView.Model

fun State.toViewModel(): Model = Model(
  login = login,
  password = password,
  authorized = authorized,
  error = error,
  loading = loading
)

fun Event.toIntent(): Intent? =
  when (this) {
    is Event.Auth -> Intent.Login(login, password)
    is Event.PasswordChanged -> Intent.HandlePasswordChanged(password)
    is Event.LoginChanged -> Intent.HandleLoginChanged(login)
    Event.DismissError -> Intent.DismissError
  }

fun Event.toOutput(): Output? =
  when (this) {
    is Event.Auth -> Output.Authorized
    is Event.PasswordChanged -> null
    is Event.LoginChanged -> null
    Event.DismissError -> null
  }