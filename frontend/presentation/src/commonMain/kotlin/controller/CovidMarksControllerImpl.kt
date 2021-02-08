package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.subject.Relay
import com.badoo.reaktive.subject.publish.PublishSubject
import mapper.*
import mapper.marksEventToIntent
import store.CovidMarksStoreFactory
import view.CovidMarksView

class CovidMarksControllerImpl(val dependencies: CovidMarksController.Dependencies) :
  CovidMarksController {

  private val marksStore = CovidMarksStoreFactory(
    storeFactory = dependencies.storeFactory,
    repository = dependencies.covidMarksRepository,
    researchId = dependencies.researchId,
    data = dependencies.data
  ).create()

  private val inputRelay: Relay<CovidMarksController.Input> = PublishSubject()
  override val input: (CovidMarksController.Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToCovidMarksIntent) bindTo marksStore
    }

    dependencies.lifecycle.doOnDestroy(marksStore::dispose)
  }

  override fun onViewCreated(
    marksView: CovidMarksView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      marksStore.labels.mapNotNull(covidMarksLabelToMarksOutput) bindTo dependencies.marksOutput
      marksView.events.mapNotNull(covidMarksEventToIntent) bindTo marksStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      marksStore.states.mapNotNull(covidMarksStateToModel) bindTo marksView
    }
  }
}