package client.newmvi.newlogin.controller

import client.domain.repository.LoginRepository
import client.newmvi.newlogin.controller.LoginController.Dependencies
import client.newmvi.newlogin.controller.LoginController.Output
import client.newmvi.newlogin.store.NewLoginStore
import client.newmvi.newlogin.store.NewLoginStoreFactory
import client.newmvi.newlogin.view.NewLoginView
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.*
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.mapNotNull

interface LoginController {

  fun onViewCreated(
    loginView: NewLoginView,
    viewLifecycle: Lifecycle,
    output: (Output) -> Unit
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val lifecycle: Lifecycle
    val repository: LoginRepository
  }

  sealed class Output {
    object Authorized : Output()
//    data class ItemSelected(val id: String) : Output()
  }
}


class NewLoginController(dependencies: Dependencies) : LoginController {

  private val loginStore =
    NewLoginStoreFactory(
      storeFactory = dependencies.storeFactory,
      repository = dependencies.repository
    ).create()

  init {
//    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
////      eventBus.mapNotNull(BusEvent::toIntent) bindTo loginStore
//      loginStore.labels.mapNotNull(TodoAddStore.Label::toBusEvent) bindTo eventBus
//    }

    dependencies.lifecycle.doOnDestroy {
      loginStore.dispose()
//      todoAddStore.dispose()
    }
  }

  override fun onViewCreated(
    loginView: NewLoginView,
    viewLifecycle: Lifecycle,
    output: (Output) -> Unit
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      loginView.events.mapNotNull(NewLoginView.Event::toIntent) bindTo loginStore
//      todoAddView.events.mapNotNull(TodoAddView.Event::toIntent) bindTo todoAddStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      loginStore.states.map(NewLoginStore.State::toViewModel) bindTo loginView
      loginView.events.mapNotNull(NewLoginView.Event::toOutput) bindTo output
    }
  }
}