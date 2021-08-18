package decompose.research.tools

import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItemWithIcon
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.themeContext
import components.researchtools.ResearchTools
import decompose.Props
import decompose.RenderableComponent
import decompose.renderableChild
import decompose.research.tools.ToolsUi.State
import kotlinx.css.*
import model.Tool
import react.RBuilder
import react.RState
import styled.StyleSheet
import styled.css
import styled.styledDiv

class ToolsUi(props: Props<ResearchTools>) : RenderableComponent<ResearchTools, State>(
  props = props,
  initialState = State(
    model = props.component.models.value
  )
) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->
      renderableChild(GridUi::class, component.grid)
      mDivider()

      styledDiv {
        css { justifyContent = JustifyContent.spaceBetween }
        mList {
          backButton()
          infoButton()
          tools(state.model.list)
          closeButton()
        }
      }
    }

  }

  private fun RBuilder.tools(items: List<Tool>) {
    items.forEach { tool ->
      mListItemWithIcon(primaryText = tool.name, iconName = tool.icon)
      when (tool) {
        is Tool.MIP -> renderableChild(MipUi::class, component.mip)
        is Tool.Brightness -> renderableChild(BrightnessUi::class, component.brightness)
        is Tool.Preset -> {
        }
//        is Tool.Preset -> renderPreset(
//          model = state.presetModel,
//          onChange = { templatesViewDelegate.dispatch(PresetView.Event.ItemClick(it)) },
//          open = props.dependencies.open
//        )
        else -> {
        }
      }
    }
  }

  private fun RBuilder.closeButton() {
    mListItemWithIcon(
      primaryText = "Закончить исследование",
      iconName = "done",
      onClick = { component }
    )
  }

  private fun RBuilder.backButton() {
    mListItemWithIcon(
      primaryText = "Все исследования",
      iconName = "keyboard_backspace",
      onClick = { component.onBackClick() }
    )
  }

  private fun RBuilder.infoButton() {
    mListItemWithIcon(
      primaryText = "Информация",
      iconName = "info",
//      onClick = { setState { infoDialogOpen = !infoDialogOpen } }
    )

//    infoDialog()
  }

//  private fun RBuilder.infoDialog() {
//
//    fun closeInfoDialog() {
//      setState { infoDialogOpen = false }
//    }
//
//    mDialog(state.infoDialogOpen, onClose = { _, _ -> closeInfoDialog() }) {
//      mDialogTitle("Команды")
//      mDialogContent {
//        styledDiv {
//          styledDiv {
//            css(ToolsComponent.ToolsStyles.dialogListItemContainer)
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemLeft); +"Окно (ширина/центр)" }
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemRight); +"СКМ (зажать)" }
//          }
//          styledDiv {
//            css(ToolsComponent.ToolsStyles.dialogListItemContainer)
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemLeft); +"Номер среза" }
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemRight); +"СКМ (крутить)" }
//          }
//          styledDiv {
//            css(ToolsComponent.ToolsStyles.dialogListItemContainer)
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemLeft); +"Разметка (эллипс)" }
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemRight); +"ЛКМ + CTRL/CMD" }
//          }
//          styledDiv {
//            css(ToolsComponent.ToolsStyles.dialogListItemContainer)
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemLeft); +"Разметка (прямоугольник)" }
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemRight); +"ЛКМ + SHIFT" }
//          }
//          styledDiv {
//            css(ToolsComponent.ToolsStyles.dialogListItemContainer)
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemLeft); +"Центр отметки" }
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemRight); +"ЛКМ + ALT" }
//          }
//          styledDiv {
//            css(ToolsComponent.ToolsStyles.dialogListItemContainer)
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemLeft); +"Удалить отметку" }
//            styledDiv { css(ToolsComponent.ToolsStyles.dialogListItemRight); +"DEL/Backspace" }
//          }
//        }
//      }
//      mDialogActions {
//        mButton("Закрыть", MColor.primary, onClick = { closeInfoDialog() })
//      }
//    }
//  }

  object ToolsStyles : StyleSheet("ToolsStyles", isStatic = true) {
    val headerStyle by css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.left
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


  class State(
    var model: ResearchTools.Model
  ) : RState
}