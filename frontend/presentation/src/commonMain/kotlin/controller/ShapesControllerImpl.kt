package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import mapper.inputToShapesIntent
import mapper.shapesEventToOutput
import mapper.shapesEventToShapesIntent
import mapper.shapesStateToShapesModel
import store.ShapesStoreFactory
import view.ShapesView

class ShapesControllerImpl(val dependencies: ShapesController.Dependencies) :
  ShapesController {

  private val shapesStore = ShapesStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    researchId = dependencies.researchId,
    repository = dependencies.researchRepository
  ).create()

  private val inputRelay: Relay<ShapesController.Input> = PublishSubject()
  override val input: (ShapesController.Input) -> Unit = inputRelay::onNext

  init {
    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToShapesIntent) bindTo shapesStore
    }

    dependencies.lifecycle.doOnDestroy { shapesStore.dispose() }
  }

  override fun onViewCreated(shapesView: ShapesView, viewLifecycle: Lifecycle) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      shapesView.events.mapNotNull(shapesEventToShapesIntent) bindTo shapesStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      shapesStore.states.mapNotNull(shapesStateToShapesModel) bindTo shapesView
      shapesView.events.mapNotNull(shapesEventToOutput) bindTo dependencies.shapesOutput
    }
  }
}
