package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import repository.LoginRepository

class LoginControllerDeps(
    override val storeFactory: StoreFactory,
    override val lifecycle: Lifecycle,
    override val loginRepository: LoginRepository,
    override val loginOutput: (LoginController.Output) -> Unit
) : LoginController.Dependencies
