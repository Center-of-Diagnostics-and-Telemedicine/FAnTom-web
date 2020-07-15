package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.debounce
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import controller.CutController.Input
import mapper.*
import store.CutStoreFactory
import store.DrawStoreFactory
import store.ShapesStoreFactory
import view.CutView
import view.DrawView
import view.ShapesView

class CutControllerImpl(val dependencies: CutController.Dependencies) :
  CutController {

  private val cutStore = CutStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    repository = dependencies.researchRepository,
    researchId = dependencies.researchId,
    brightnessRepository = dependencies.brightnessRepository,
    mipRepository = dependencies.mipRepository
  ).create()

  private val shapesStore = ShapesStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    researchId = dependencies.researchId,
    repository = dependencies.researchRepository
  ).create()

  private val drawStore = DrawStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    researchId = dependencies.researchId
  ).create()

  private val inputRelay: Relay<Input> = PublishSubject()
  override val input: (Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToCutIntent) bindTo cutStore
      drawStore.labels.mapNotNull(drawLabelToCutIntent) bindTo cutStore
      shapesStore.labels.mapNotNull(shapesLabelToCutIntent) bindTo cutStore
      cutStore.labels.mapNotNull(cutLabelToShapesIntent) bindTo shapesStore
      drawStore.labels.mapNotNull(drawLabelToShapesIntent) bindTo shapesStore
      drawStore.labels
        .debounce(60, mainScheduler)
        .mapNotNull(drawDebounceLabelToShapesIntent) bindTo shapesStore
    }

    dependencies.lifecycle.doOnDestroy(cutStore::dispose)
  }

  override fun onViewCreated(
    cutView: CutView,
    shapesView: ShapesView,
    drawView: DrawView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      cutStore.labels.mapNotNull(cutLabelToCutOutput) bindTo dependencies.cutOutput

      shapesView.events.mapNotNull(shapesEventToShapesIntent) bindTo shapesStore
      drawView.events.mapNotNull(drawEventToDrawIntent) bindTo drawStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      cutStore.states.mapNotNull(cutStateToCutModel) bindTo cutView
      shapesStore.states.mapNotNull(shapesStateToShapesModel) bindTo shapesView
      drawStore.states.mapNotNull(drawStateToDrawModel) bindTo drawView
    }
  }
}
