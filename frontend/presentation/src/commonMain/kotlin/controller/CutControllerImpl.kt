package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import controller.CutController.Input
import mapper.cutStateToCutModel
import mapper.inputToCutIntent
import store.CutStoreFactory
import view.CutView

class CutControllerImpl(val dependencies: CutController.Dependencies) :
  CutController {

  private val cutStore = CutStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    repository = dependencies.researchRepository,
    researchId = dependencies.researchId
  ).create()
//  private val sliderStore = SliderStoreFactory(
//    storeFactory = dependencies.storeFactory,
//    cut = dependencies.cut,
//    researchId = dependencies.researchId
//  ).create()

  private val inputRelay: Relay<Input> = PublishSubject()
  override val input: (Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
//      sliderStore.labels.mapNotNull(sliderLabelToCutIntent) bindTo cutStore
      inputRelay.mapNotNull(inputToCutIntent) bindTo cutStore
    }

    dependencies.lifecycle.doOnDestroy {
      cutStore.dispose()
//      sliderStore.dispose()
    }
  }

  override fun onViewCreated(cutView: CutView, viewLifecycle: Lifecycle) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
//      sliderView.events.mapNotNull(slideEventToSlideIntent) bindTo sliderStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      cutStore.states.mapNotNull(cutStateToCutModel) bindTo cutView
//      sliderStore.states.mapNotNull(sliderStateToSliderModel) bindTo sliderView
    }
  }
}
