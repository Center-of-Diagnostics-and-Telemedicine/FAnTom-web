package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import repository.LoginRepository
import view.LoginView

interface LoginController {

  fun onViewCreated(
      loginView: LoginView,
      viewLifecycle: Lifecycle,
      output: (Output) -> Unit
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val loginRepository: LoginRepository
  }

  sealed class Output {
    object Authorized : Output()
  }
}
