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
import mapper.inputToMarksIntent
import mapper.marksEventToIntent
import mapper.marksLabelToMarksOutput
import mapper.marksStateToModel
import store.MarksStoreFactory
import view.MarksView

class MarksControllerImpl(val dependencies: MarksController.Dependencies) :
  MarksController {

  private val marksStore = MarksStoreFactory(
    storeFactory = dependencies.storeFactory,
    repository = dependencies.marksRepository,
    research = dependencies.research,
    data = dependencies.data
  ).create()

  private val inputRelay: Relay<MarksController.Input> = PublishSubject()
  override val input: (MarksController.Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToMarksIntent) bindTo marksStore
    }

    dependencies.lifecycle.doOnDestroy(marksStore::dispose)
  }

  override fun onViewCreated(
    marksView: MarksView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      marksStore.labels.mapNotNull(marksLabelToMarksOutput) bindTo dependencies.marksOutput
      marksView.events.mapNotNull(marksEventToIntent) bindTo marksStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      marksStore.states.mapNotNull(marksStateToModel) bindTo marksView
    }
  }
}
