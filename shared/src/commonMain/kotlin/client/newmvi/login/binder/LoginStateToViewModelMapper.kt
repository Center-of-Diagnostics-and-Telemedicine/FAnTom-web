package client.newmvi.login.binder

import client.newmvi.login.store.LoginStore
import client.newmvi.login.view.LoginView

object LoginStateToViewModelMapper {

  operator fun invoke(state: LoginStore.State): LoginView.LoginViewModel =
    LoginView.LoginViewModel(
      isLoading = state.isLoading,
      error = state.error,
      authorized = state.authorized
    )
}