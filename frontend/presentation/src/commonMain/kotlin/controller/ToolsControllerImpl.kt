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
import store.*
import view.*

class ToolsControllerImpl(val dependencies: ToolsController.Dependencies) : ToolsController {

  private val toolsStore = ToolsStoreFactory(
    storeFactory = dependencies.storeFactory
  ).create()

  private val gridStore = GridStoreFactory(
    storeFactory = dependencies.storeFactory,
    data = dependencies.data
  ).create()

  private val mipStore = MipStoreFactory(
    storeFactory = dependencies.storeFactory,
    mipRepository = dependencies.mipRepository
  ).create()

  private val presetStore = PresetStoreFactory(
    storeFactory = dependencies.storeFactory,
    data = dependencies.data,
    brightnessRepository = dependencies.brightnessRepository
  ).create()

  private val brightnessStore = BrightnessStoreFactory(
    storeFactory = dependencies.storeFactory,
    brightnessRepository = dependencies.brightnessRepository
  ).create()

  private val inputRelay: Relay<ToolsController.Input> = PublishSubject()
  override val input: (ToolsController.Input) -> Unit = inputRelay::onNext

  init {

    bind(dependencies.lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      inputRelay.mapNotNull(toolsInputToBrightnessIntent) bindTo brightnessStore
      inputRelay.mapNotNull(toolsInputToGridIntent) bindTo gridStore
    }

    dependencies.lifecycle.doOnDestroy {
      toolsStore.dispose()
      gridStore.dispose()
      mipStore.dispose()
      brightnessStore.dispose()
      presetStore.dispose()
    }
  }

  override fun onViewCreated(
    toolsView: ToolsView,
    gridView: GridView,
    mipView: MipView,
    brightnessView: BrightnessView,
    presetView: PresetView,
    viewLifecycle: Lifecycle,
  ) {
    bind(viewLifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      toolsView.events.mapNotNull(toolsEventToToolsIntent) bindTo toolsStore
      gridView.events.mapNotNull(gridEventToGridIntent) bindTo gridStore
      mipView.events.mapNotNull(mipEventToMipIntent) bindTo mipStore
      brightnessView.events.mapNotNull(brightnessEventToBrightnessIntent) bindTo brightnessStore
      presetView.events.mapNotNull(presetEventToPresetIntent) bindTo presetStore
    }

    bind(viewLifecycle, BinderLifecycleMode.START_STOP) {
      toolsStore.states.mapNotNull(toolsStateToToolsModel) bindTo toolsView
      gridStore.states.mapNotNull(gridStateToGridModel) bindTo gridView
      mipStore.states.mapNotNull(mipStateToMipModel) bindTo mipView
      brightnessStore.states.mapNotNull(brightnessStateToBrightnessModel) bindTo brightnessView
      presetStore.states.mapNotNull(presetStateToPresetModel) bindTo presetView
      toolsView.events.mapNotNull(toolsEventToToolsOutput) bindTo dependencies.toolsOutput

      presetStore.labels.mapNotNull(presetLabelToBrightnessIntent) bindTo brightnessStore

      gridStore.labels.mapNotNull(gridLabelToToolsOutput) bindTo dependencies.toolsOutput
      mipStore.labels.mapNotNull(mipLabelToToolsOutput) bindTo dependencies.toolsOutput
      brightnessStore.labels.mapNotNull(brightnessLabelToToolsOutput) bindTo dependencies.toolsOutput
      presetStore.labels.mapNotNull(presetLabelToToolsOutput) bindTo dependencies.toolsOutput
    }
  }
}
