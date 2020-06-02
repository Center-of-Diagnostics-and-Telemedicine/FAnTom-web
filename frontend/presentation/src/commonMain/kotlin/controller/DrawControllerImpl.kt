package controller

import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.events
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.arkivanov.mvikotlin.extensions.reaktive.states
import com.badoo.reaktive.observable.mapNotNull
import mapper.drawEventToDrawIntent
import mapper.drawLabelToOutput
import mapper.drawStateToDrawModel
import store.DrawStoreFactory
import view.DrawView

class DrawControllerImpl(val dependencies: DrawController.Dependencies) :
  DrawController {

  private val drawStore = DrawStoreFactory(
    storeFactory = dependencies.storeFactory,
    cut = dependencies.cut,
    researchId = dependencies.researchId
  ).create()

  init {
    dependencies.lifecycle.doOnDestroy { drawStore.dispose() }
  }

  override fun onViewCreated(drawView: DrawView, viewLifecycle: Lifecycle) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      drawView.events.mapNotNull(drawEventToDrawIntent) bindTo drawStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      drawStore.states.mapNotNull(drawStateToDrawModel) bindTo drawView
      drawStore.labels.mapNotNull(drawLabelToOutput) bindTo dependencies.drawOutput
//      sliderStore.states.mapNotNull(sliderStateToDrawModel) bindTo sliderView
    }
  }
}
