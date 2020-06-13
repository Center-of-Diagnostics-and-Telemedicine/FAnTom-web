package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import mapper.*
import store.MarksStoreFactory
import view.MarksView

class MarksControllerImpl(val dependencies: MarksController.Dependencies) :
  MarksController {

  private val marksStore = MarksStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    repository = dependencies.researchRepository,
    researchId = dependencies.researchId
  ).create()

  private val inputRelay: Relay<MarksController.Input> = PublishSubject()
  override val input: (MarksController.Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToMarksIntent) bindTo marksStore
//      drawStore.labels.mapNotNull(drawLabelToMarksIntent) bindTo cutStore
//      drawStore.labels.mapNotNull(drawLabelToShapesIntent) bindTo shapesStore
//      shapesStore.labels.mapNotNull(shapesLabelToMarksIntent) bindTo cutStore
      marksStore.labels.mapNotNull(cutLabelToShapesIntent) bindTo shapesStore

    }

    dependencies.lifecycle.doOnDestroy(marksStore::dispose)
  }

  override fun onViewCreated(
    marksView: MarksView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
//      cutStore.labels.mapNotNull(cutLabelToMarksOutput) bindTo dependencies.cutOutput
//
//      shapesView.events.mapNotNull(shapesEventToShapesIntent) bindTo shapesStore
//      drawView.events.mapNotNull(drawEventToDrawIntent) bindTo drawStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      marksStore.states.mapNotNull(cutStateToMarksModel) bindTo cutView
//      shapesStore.states.mapNotNull(shapesStateToShapesModel) bindTo shapesView
//      drawStore.states.mapNotNull(drawStateToDrawModel) bindTo drawView
    }
  }
}
