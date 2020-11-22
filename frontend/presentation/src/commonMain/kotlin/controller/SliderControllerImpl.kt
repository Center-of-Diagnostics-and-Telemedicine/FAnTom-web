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
import controller.SliderController.Dependencies
import controller.SliderController.Input
import mapper.*
import store.SliderStoreFactory
import view.SliderView

class SliderControllerImpl(val dependencies: Dependencies) :
  SliderController {

  private val sliderStore = SliderStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    researchId = dependencies.researchId
  ).create()

  private val inputRelay: Relay<Input> = PublishSubject()
  override val input: (Input) -> Unit = inputRelay::onNext

  init {
    dependencies.lifecycle.doOnDestroy { sliderStore.dispose() }
  }

  override fun onViewCreated(sliderView: SliderView, viewLifecycle: Lifecycle) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToSliderIntent) bindTo sliderStore
      sliderView.events.mapNotNull(sliderEventToSlideIntent) bindTo sliderStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      sliderStore.states.mapNotNull(sliderStateToSliderModel) bindTo sliderView
      sliderView.events.mapNotNull(sliderEventToOutput) bindTo dependencies.sliderOutput
    }
  }
}
