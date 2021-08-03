package research.tools

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.MColor
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.dialog.mDialog
import com.ccfraser.muirwik.components.dialog.mDialogActions
import com.ccfraser.muirwik.components.dialog.mDialogContent
import com.ccfraser.muirwik.components.dialog.mDialogTitle
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.spacingUnits
import controller.ToolsController
import controller.ToolsControllerImpl
import destroy
import kotlinx.css.*
import model.ResearchData
import model.Tool
import react.*
import repository.BrightnessRepository
import repository.MipRepository
import research.tools.ToolsComponent.ToolsStyles.dialogListItemContainer
import research.tools.ToolsComponent.ToolsStyles.dialogListItemLeft
import research.tools.ToolsComponent.ToolsStyles.dialogListItemRight
import research.tools.brightness.BrightnessViewProxy
import research.tools.brightness.renderBrightness
import research.tools.grid.GridViewProxy
import research.tools.mip.MipViewProxy
import research.tools.mip.renderMip
import research.tools.preset.PresetViewProxy
import research.tools.preset.renderPreset
import resume
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.*

class ToolsComponent(prps: ToolsProps) : RComponent<ToolsProps, ToolsState>(prps) {

  private val gridViewDelegate = GridViewProxy(::updateState)
  private val mipViewDelegate = MipViewProxy(::updateState)
  private val brightnessViewDelegate = BrightnessViewProxy(::updateState)
  private val templatesViewDelegate = PresetViewProxy(::updateState)
  private val toolsViewDelegate = ToolsViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ToolsController

  init {
    state = ToolsState(
      toolsModel = initialToolsModel(),
      gridModel = initialGridModel(
        type = props.dependencies.data.type,
        doseReport = props.dependencies.data.doseReport,
        data = props.dependencies.data
      ),
      mipModel = initialMipModel(),
      brightnessModel = initialBrightnessModel(),
      presetModel = initialPresetModel(),
      infoDialogOpen = false
    )
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    val dependencies = props.dependencies
    val disposable = dependencies.toolsInput.subscribe { controller.input(it) }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
    controller.onViewCreated(
      gridView = gridViewDelegate,
      mipView = mipViewDelegate,
      brightnessView = brightnessViewDelegate,
      presetView = templatesViewDelegate,
      toolsView = toolsViewDelegate,
      viewLifecycle = lifecycleRegistry
    )
  }

  private fun createController(): ToolsController {
    val dependencies = props.dependencies
    val toolsControllerDependencies =
      object : ToolsController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return ToolsControllerImpl(toolsControllerDependencies)
  }

  override fun RBuilder.render() {
//    styledDiv {
//      css(headerStyle)
//      grid(
//        current = state.gridModel.current,
//        onClick = { gridViewDelegate.dispatch(GridView.Event.ItemClick(it)) }
//      )
//    }

    mDivider()

    styledDiv {
      css { justifyContent = JustifyContent.spaceBetween }
      mList {
        backButton()
        infoButton()
        tools(state.toolsModel.items)
        closeButton()
      }
    }
  }

  private fun RBuilder.tools(items: List<Tool>) {
    items.forEach { tool ->
      mListItemWithIcon(primaryText = tool.name, iconName = tool.icon)
      when (tool) {
        is Tool.MIP -> renderMip(
          model = state.mipModel,
          onClick = { mipViewDelegate.dispatch(MipView.Event.ItemClick(it)) },
          onChange = { mipViewDelegate.dispatch(MipView.Event.MipValueChanged(it)) },
          open = props.dependencies.open
        )
        is Tool.Brightness -> renderBrightness(
          model = state.brightnessModel,
          onBlackChange = { brightnessViewDelegate.dispatch(BrightnessView.Event.BlackChanged(it)) },
          onWhiteChange = { brightnessViewDelegate.dispatch(BrightnessView.Event.WhiteChanged(it)) },
          onGammaChange = { brightnessViewDelegate.dispatch(BrightnessView.Event.GammaChanged(it)) },
          open = props.dependencies.open
        )
        is Tool.Preset -> renderPreset(
          model = state.presetModel,
          onChange = { templatesViewDelegate.dispatch(PresetView.Event.ItemClick(it)) },
          open = props.dependencies.open
        )
      }
    }
  }

  private fun RBuilder.closeButton() {
    mListItemWithIcon(
      primaryText = "Закончить исследование",
      iconName = "done",
      onClick = { toolsViewDelegate.dispatch(ToolsView.Event.CloseClick) }
    )
  }

  private fun RBuilder.backButton() {
    mListItemWithIcon(
      primaryText = "Все исследования",
      iconName = "keyboard_backspace",
      onClick = { toolsViewDelegate.dispatch(ToolsView.Event.BackClick) }
    )
  }

  private fun RBuilder.infoButton() {
    mListItemWithIcon(
      primaryText = "Информация",
      iconName = "info",
      onClick = { setState { infoDialogOpen = !infoDialogOpen } }
    )

    infoDialog()
  }

  private fun RBuilder.infoDialog() {

    fun closeInfoDialog() {
      setState { infoDialogOpen = false }
    }

    mDialog(state.infoDialogOpen, onClose = { _, _ -> closeInfoDialog() }) {
      mDialogTitle("Команды")
      mDialogContent {
        styledDiv {
          styledDiv {
            css(dialogListItemContainer)
            styledDiv { css(dialogListItemLeft); +"Окно (ширина/центр)" }
            styledDiv { css(dialogListItemRight); +"СКМ (зажать)" }
          }
          styledDiv {
            css(dialogListItemContainer)
            styledDiv { css(dialogListItemLeft); +"Номер среза" }
            styledDiv { css(dialogListItemRight); +"СКМ (крутить)" }
          }
          styledDiv {
            css(dialogListItemContainer)
            styledDiv { css(dialogListItemLeft); +"Разметка (эллипс)" }
            styledDiv { css(dialogListItemRight); +"ЛКМ + CTRL/CMD" }
          }
          styledDiv {
            css(dialogListItemContainer)
            styledDiv { css(dialogListItemLeft); +"Разметка (прямоугольник)" }
            styledDiv { css(dialogListItemRight); +"ЛКМ + SHIFT" }
          }
          styledDiv {
            css(dialogListItemContainer)
            styledDiv { css(dialogListItemLeft); +"Центр отметки" }
            styledDiv { css(dialogListItemRight); +"ЛКМ + ALT" }
          }
          styledDiv {
            css(dialogListItemContainer)
            styledDiv { css(dialogListItemLeft); +"Удалить отметку" }
            styledDiv { css(dialogListItemRight); +"DEL/Backspace" }
          }
        }
      }
      mDialogActions {
        mButton("Закрыть", MColor.primary, onClick = { closeInfoDialog() })
      }
    }
  }

  private fun updateState(model: ToolsView.Model) = setState { toolsModel = model }
  private fun updateState(model: GridView.Model) = setState { gridModel = model }
  private fun updateState(model: MipView.Model) = setState { mipModel = model }
  private fun updateState(model: BrightnessView.Model) = setState { brightnessModel = model }
  private fun updateState(model: PresetView.Model) = setState { presetModel = model }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val toolsOutput: (ToolsController.Output) -> Unit
    val toolsInput: Observable<ToolsController.Input>
    val brightnessRepository: BrightnessRepository
    val mipRepository: MipRepository
    val open: Boolean
    val data: ResearchData
  }

  object ToolsStyles : StyleSheet("ToolsStyles", isStatic = true) {
    val headerStyle by css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.left
    }

    val nested by css {
      paddingLeft = 4.spacingUnits
    }

    val dialogListItemContainer by css {
      display = Display.flex
      alignItems = Align.flexStart
      flexDirection = FlexDirection.row
      justifyContent = JustifyContent.spaceBetween
    }

    val dialogListItemRight by css {
      marginLeft = 2.spacingUnits
    }

    val dialogListItemLeft by css {
      marginRight = 2.spacingUnits
    }
  }

}

class ToolsState(
  var toolsModel: ToolsView.Model,
  var gridModel: GridView.Model,
  var mipModel: MipView.Model,
  var brightnessModel: BrightnessView.Model,
  var presetModel: PresetView.Model,
  var infoDialogOpen: Boolean
) : RState

interface ToolsProps : RProps {
  var dependencies: ToolsComponent.Dependencies
}

fun RBuilder.tools(dependencies: ToolsComponent.Dependencies) = child(ToolsComponent::class) {
  attrs.dependencies = dependencies
}
