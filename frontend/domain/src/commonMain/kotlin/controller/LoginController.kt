package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import repository.LoginRepository
import view.LoginView

interface LoginController {

  fun onViewCreated(
    loginView: LoginView,
    viewLifecycle: Lifecycle
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val loginRepository: LoginRepository
    val loginOutput: (Output) -> Unit
  }

  sealed class Output {
    object Authorized : Output()
  }
}
