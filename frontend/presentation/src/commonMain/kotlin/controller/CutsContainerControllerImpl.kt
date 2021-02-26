package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import mapper.CutsContainerStateToCutsContainerModel
import mapper.inputToCutsContainerIntent
import store.CutsContainerStoreFactory
import view.CutsContainerView

class CutsContainerControllerImpl(val dependencies: CutsContainerController.Dependencies) :
  CutsContainerController {

  private val inputRelay: Relay<CutsContainerController.Input> = PublishSubject()
  override val input: (CutsContainerController.Input) -> Unit = inputRelay::onNext

  private val gridContainerStore =
    CutsContainerStoreFactory(
      storeFactory = dependencies.storeFactory,
      data = dependencies.data
    ).create()

  init {
    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToCutsContainerIntent) bindTo gridContainerStore
    }


    dependencies.lifecycle.doOnDestroy {
      gridContainerStore.dispose()
    }
  }

  override fun onViewCreated(cutsContainerView: CutsContainerView, viewLifecycle: Lifecycle) {
//    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
//      gridContainerView.events.mapNotNull(gridContainerEventToGridIntent) bindTo gridContainerStore
//    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      gridContainerStore.states.mapNotNull(CutsContainerStateToCutsContainerModel) bindTo cutsContainerView
    }
  }
}
