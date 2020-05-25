package research.tools

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.spacingUnits
import controller.ToolsController
import controller.ToolsControllerImpl
import kotlinx.css.*
import list.ListScreen
import model.Tool
import react.*
import research.tools.ToolsViewComponent.ToolsStyles.headerStyle
import research.tools.brightness.BrightnessViewProxy
import research.tools.brightness.renderBrightness
import research.tools.grid.GridViewProxy
import research.tools.grid.grid
import research.tools.mip.MipViewProxy
import research.tools.mip.renderMip
import research.tools.preset.PresetViewProxy
import research.tools.preset.renderPreset
import root.LifecycleWrapper
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.*

class ToolsViewComponent(prps: ToolsProps) : RComponent<ToolsProps, ToolsState>(prps) {

  private val gridViewDelegate = GridViewProxy(::updateState)
  private val mipViewDelegate = MipViewProxy(::updateState)
  private val brightnessViewDelegate = BrightnessViewProxy(::updateState)
  private val templatesViewDelegate = PresetViewProxy(::updateState)
  private val toolsViewDelegate = ToolsViewProxy(::updateState)
  private val lifecycleWrapper = LifecycleWrapper()
  private lateinit var controller: ToolsController

  init {
    state = ToolsState(
      toolsModel = initialToolsModel(),
      gridModel = initialGridModel(),
      mipModel = initialMipModel(),
      brightnessModel = initialBrightnessModel(),
      presetModel = initialPresetModel()
    )
  }

  override fun componentDidMount() {
    lifecycleWrapper.start()
    controller = createController()
    controller.onViewCreated(
      gridView = gridViewDelegate,
      mipView = mipViewDelegate,
      brightnessView = brightnessViewDelegate,
      presetView = templatesViewDelegate,
      toolsView = toolsViewDelegate,
      viewLifecycle = lifecycleWrapper.lifecycle
    )
  }

  private fun createController(): ToolsController {
    val dependencies = props.dependencies
    val toolsControllerDependencies =
      object : ToolsController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleWrapper.lifecycle
      }
    return ToolsControllerImpl(toolsControllerDependencies)
  }

  override fun RBuilder.render() {
    styledDiv {
      css(headerStyle)
      grid(
        current = state.gridModel.current,
        onClick = { gridViewDelegate.dispatch(GridView.Event.ItemClick(it)) }
      )
    }

    mDivider()

    styledDiv {
      css { justifyContent = JustifyContent.spaceBetween }
      mList {

        renderTools(state.toolsModel.items)

        mListItemWithIcon(
          primaryText = "Все исследования",
          iconName = "keyboard_backspace",
          onClick = { toolsViewDelegate.dispatch(ToolsView.Event.CloseClick) }
        )
      }
    }
  }

  private fun RBuilder.renderTools(items: List<Tool>) {
    items.forEach { tool ->
      mListItemWithIcon(primaryText = tool.name, iconName = tool.icon)
      when (tool) {
        is Tool.MIP -> renderMip(
          model = state.mipModel,
          onClick = { mipViewDelegate.dispatch(MipView.Event.ItemClick(it)) },
          onChange = { mipViewDelegate.dispatch(MipView.Event.MipValueChanged(it)) }
        )
        is Tool.Brightness -> renderBrightness(
          model = state.brightnessModel,
          onBlackChange = { brightnessViewDelegate.dispatch(BrightnessView.Event.BlackChanged(it)) },
          onWhiteChange = { brightnessViewDelegate.dispatch(BrightnessView.Event.WhiteChanged(it)) },
          onGammaChange = { brightnessViewDelegate.dispatch(BrightnessView.Event.GammaChanged(it)) }
        )
        is Tool.Preset -> renderPreset(
          model = state.presetModel,
          onChange = { templatesViewDelegate.dispatch(PresetView.Event.ItemClick(it)) }
        )
      }
    }
  }

  private fun updateState(model: ToolsView.Model) = setState { toolsModel = model }
  private fun updateState(model: GridView.Model) = setState { gridModel = model }
  private fun updateState(model: MipView.Model) = setState { mipModel = model }
  private fun updateState(model: BrightnessView.Model) = setState { brightnessModel = model }
  private fun updateState(model: PresetView.Model) = setState { presetModel = model }

  interface Dependencies {
    val output: (ToolsController.Output) -> Unit
    val storeFactory: StoreFactory
  }

  object ToolsStyles : StyleSheet("ToolsStyles", isStatic = true) {
    val headerStyle by css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.flexEnd
    }

    val nested by ListScreen.ListStyles.css {
      paddingLeft = 4.spacingUnits
    }
  }

}

class ToolsState(
  var toolsModel: ToolsView.Model,
  var gridModel: GridView.Model,
  var mipModel: MipView.Model,
  var brightnessModel: BrightnessView.Model,
  var presetModel: PresetView.Model
) : RState

interface ToolsProps : RProps {
  var dependencies: ToolsViewComponent.Dependencies
}

fun RBuilder.tools(dependencies: ToolsViewComponent.Dependencies) = child(ToolsViewComponent::class) {
  attrs.dependencies = dependencies
}
