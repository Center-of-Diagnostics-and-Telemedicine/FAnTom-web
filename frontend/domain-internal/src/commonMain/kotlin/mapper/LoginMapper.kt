package mapper

import controller.LoginController.Output
import store.LoginStore.*
import view.LoginView.Event
import view.LoginView.Model

val loginStateToLoginModel: State.() -> Model? = {
    Model(
        login = login,
        password = password,
        error = error,
        loading = loading
    )
}

val loginEventToLoginIntent: Event.() -> Intent? = {
    when (this) {
        is Event.Auth -> Intent.Auth
        is Event.PasswordChanged -> Intent.HandlePasswordChanged(password)
        is Event.LoginChanged -> Intent.HandleLoginChanged(login)
        Event.DismissError -> Intent.DismissError
    }
}

val loginLabelToOutput: Label.() -> Output? = {
    when (this) {
        Label.Authorized -> Output.Authorized
    }
}