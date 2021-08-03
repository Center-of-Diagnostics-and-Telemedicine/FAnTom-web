package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
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
import model.COVID_RESEARCH_CATEGORY
import store.CutStoreFactory
import store.DrawStoreFactory
import store.ShapesStoreFactory
import store.UserInputStoreFactory
import store.draw.DrawStore
import store.userinput.UserInputStore
import view.CutView
import view.DrawView
import view.ShapesView

class CutControllerImpl(val dependencies: CutController.Dependencies) :
  CutController {

  private val isDrawEnabled = dependencies.research.category != COVID_RESEARCH_CATEGORY

  private val cutStore = CutStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    repository = dependencies.researchRepository,
    research = dependencies.research,
    brightnessRepository = dependencies.brightnessRepository,
    mipRepository = dependencies.mipRepository
  ).create()

  private val shapesStore = ShapesStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    research = dependencies.research,
    repository = dependencies.researchRepository
  ).create()

  private val drawStore: DrawStore? =
    if (isDrawEnabled)
      DrawStoreFactory(
        storeFactory = dependencies.storeFactory,
        cut = dependencies.cut,
        research = dependencies.research
      ).create()
    else null

  private val userInputStore: UserInputStore? =
    if (isDrawEnabled.not())
      UserInputStoreFactory(
        storeFactory = dependencies.storeFactory,
        cut = dependencies.cut,
        research = dependencies.research
      ).create()
    else null

  private val inputRelay: Relay<Input> = PublishSubject()
  override val input: (Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToCutIntent) bindTo cutStore

      shapesStore.labels.mapNotNull(shapesLabelToCutIntent) bindTo cutStore

      cutStore.labels.mapNotNull(cutLabelToShapesIntent) bindTo shapesStore

      drawStore?.labels?.mapNotNull(drawLabelToCutIntent)?.bindTo(cutStore)
      drawStore?.labels?.mapNotNull(drawLabelToShapesIntent)?.bindTo(shapesStore)
      drawStore
        ?.labels
        ?.debounce(60, mainScheduler)
        ?.mapNotNull(drawDebounceLabelToShapesIntent)
        ?.bindTo(shapesStore)

      userInputStore?.labels?.mapNotNull(userInputToCutIntent)?.bindTo(cutStore)
      userInputStore
        ?.labels
        ?.debounce(60, mainScheduler)
        ?.mapNotNull(userInputDebounceLabelToShapesIntent)
        ?.bindTo(shapesStore)
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

      drawStore?.let {
        drawView.events.mapNotNull(drawEventToDrawIntent) bindTo drawStore
      }

      userInputStore?.let {
        drawView.events.mapNotNull(drawEventToUserInputIntent) bindTo userInputStore
      }

      cutView.events.mapNotNull(cutEventToCutIntent) bindTo cutStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      cutStore.states.mapNotNull(cutStateToCutModel) bindTo cutView

      shapesStore.states.mapNotNull(shapesStateToShapesModel) bindTo shapesView

      drawStore?.states?.mapNotNull(drawStateToDrawModel)?.bindTo(drawView)

      userInputStore?.states?.mapNotNull(userInputStateToDrawModel)?.bindTo(drawView)
    }
  }
}
