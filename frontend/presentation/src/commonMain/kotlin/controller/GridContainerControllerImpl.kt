package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import mapper.gridContainerStateToGridContainerModel
import mapper.inputToGridContainerIntent
import store.GridContainerStoreFactory
import view.GridContainerView

class GridContainerControllerImpl(val dependencies: GridContainerController.Dependencies) :
  GridContainerController {

  private val inputRelay: Relay<GridContainerController.Input> = PublishSubject()
  override val input: (GridContainerController.Input) -> Unit = inputRelay::onNext

  private val gridContainerStore =
    GridContainerStoreFactory(
      storeFactory = dependencies.storeFactory,
      data = dependencies.data
    ).create()

  init {
    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToGridContainerIntent) bindTo gridContainerStore
    }


    dependencies.lifecycle.doOnDestroy {
      gridContainerStore.dispose()
    }
  }

  override fun onViewCreated(gridContainerView: GridContainerView, viewLifecycle: Lifecycle) {
//    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
//      gridContainerView.events.mapNotNull(gridContainerEventToGridIntent) bindTo gridContainerStore
//    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      gridContainerStore.states.mapNotNull(gridContainerStateToGridContainerModel) bindTo gridContainerView
    }
  }
}
