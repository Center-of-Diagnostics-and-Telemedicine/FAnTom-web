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
import mapper.expertMarksEventToIntent
import mapper.expertMarksLabelToMarksOutput
import mapper.expertMarksStateToModel
import mapper.inputToExpertMarksIntent
import store.ExpertMarksStoreFactory
import view.ExpertMarksView

class ExpertMarksControllerImpl(val dependencies: ExpertMarksController.Dependencies) :
  ExpertMarksController {

  private val marksStore = ExpertMarksStoreFactory(
    storeFactory = dependencies.storeFactory,
    research = dependencies.research,
    data = dependencies.data,
    expertMarksRepository = dependencies.expertMarksRepository
  ).create()

  private val inputRelay: Relay<ExpertMarksController.Input> = PublishSubject()
  override val input: (ExpertMarksController.Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(inputToExpertMarksIntent) bindTo marksStore
    }

    dependencies.lifecycle.doOnDestroy(marksStore::dispose)
  }

  override fun onViewCreated(
    expertMarksView: ExpertMarksView,
    viewLifecycle: Lifecycle
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      marksStore.labels.mapNotNull(expertMarksLabelToMarksOutput) bindTo dependencies.expertMarksOutput
      expertMarksView.events.mapNotNull(expertMarksEventToIntent) bindTo marksStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      marksStore.states.mapNotNull(expertMarksStateToModel) bindTo expertMarksView
    }
  }
}