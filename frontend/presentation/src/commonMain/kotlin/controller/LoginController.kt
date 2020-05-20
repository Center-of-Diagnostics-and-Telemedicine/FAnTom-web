package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import controller.LoginController.Dependencies
import controller.LoginController.Output
import mapper.loginEventToLoginIntent
import mapper.loginLabelToOutput
import mapper.loginStateToLoginModel
import store.LoginStoreFactory
import view.LoginView

class LoginControllerImpl(dependencies: Dependencies) : LoginController {

    private val loginStore =
        LoginStoreFactory(
            storeFactory = dependencies.storeFactory,
            repository = dependencies.loginRepository
        ).create()

    init {
        dependencies.lifecycle.doOnDestroy(loginStore::dispose)
    }

    override fun onViewCreated(
        loginView: LoginView,
        viewLifecycle: Lifecycle,
        output: (Output) -> Unit
    ) {
        bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
            loginView.events.mapNotNull(loginEventToLoginIntent) bindTo loginStore
        }

        bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
            loginStore.states.mapNotNull(loginStateToLoginModel) bindTo loginView
            loginStore.labels.mapNotNull(loginLabelToOutput) bindTo output
        }
    }
}
