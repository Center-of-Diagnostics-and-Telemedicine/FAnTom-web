package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import mapper.researchEventToResearchIntent
import mapper.researchLabelToResearchOutput
import mapper.researchStateToResearchModel
import store.ResearchStoreFactory
import view.ResearchView

class ResearchControllerImpl(val dependencies: ResearchController.Dependencies) :
  ResearchController {

  private val researchStore =
    ResearchStoreFactory(
      storeFactory = dependencies.storeFactory,
      repository = dependencies.researchRepository,
      researchId = dependencies.researchId
    ).create()

  init {
    dependencies.lifecycle.doOnDestroy { researchStore.dispose() }
  }

  override fun onViewCreated(researchView: ResearchView, viewLifecycle: Lifecycle) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      researchView.events.mapNotNull(researchEventToResearchIntent) bindTo researchStore
      researchStore.labels.mapNotNull(researchLabelToResearchOutput) bindTo dependencies.researchOutput
//      researchView.events.mapNotNull(researchEventToOutput) bindTo dependencies.researchOutput
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      researchStore.states.mapNotNull(researchStateToResearchModel) bindTo researchView
    }
  }
}
