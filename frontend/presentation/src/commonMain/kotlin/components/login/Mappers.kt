package components.login

import components.login.Login.Model
import components.login.Login.Output
import store.login.LoginStore.Label
import store.login.LoginStore.State

internal val stateToModel: (State) -> Model =
  {
    Model(
      login = it.login,
      password = it.password,
      error = it.error,
      loading = it.loading
    )
  }

internal val labelsToOutput: (Label) -> Output =
  {
    when (it) {
      Label.Authorized -> Output.Authorized
    }
  }