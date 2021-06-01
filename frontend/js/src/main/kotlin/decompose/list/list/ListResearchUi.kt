package decompose.list.list

import com.ccfraser.muirwik.components.*
import decompose.list.list.ListResearchUi.State
import components.list.ResearchList
import decompose.RenderableComponent
import kotlinx.css.*
import list.*
import model.Category
import model.Filter
import model.Research
import react.RBuilder
import react.RState
import react.setState
import styled.StyleSheet
import styled.styledDiv
import view.CategoryView
import view.FilterView
import view.ListView

class ListResearchUi(props: Props<ResearchList>) : RenderableComponent<ResearchList, State>(
  props = props,
  initialState = State(model = props.component.models.value)
) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      mTypography { +"this is research list ui" }
    }
  }

  object ListStyles : StyleSheet("ListScreenStyles", isStatic = true) {

    val screenContainerCss by ListStyles.css {
      flexGrow = 1.0
      width = 100.pct
      zIndex = 0
      overflow = Overflow.hidden
      position = Position.relative
      display = Display.flex
    }

    val appFrameCss by ListStyles.css {
      overflow = Overflow.hidden
      position = Position.relative
      display = Display.flex
      height = 100.pct
      width = 100.pct
    }

    val appBarSpacerCss by ListStyles.css {
      display = Display.flex
      alignItems = Align.center
      justifyContent = JustifyContent.flexEnd
      height = 64.px
    }

    val mainContainerCss by ListStyles.css {
      padding(16.px)
      height = 100.pct
      width = 100.pct
      flexGrow = 1.0
      minWidth = 0.px
    }

    val categoriesContainerCss by ListStyles.css {
      display = Display.flex
      justifyContent = JustifyContent.start
      flexWrap = FlexWrap.wrap
      marginBottom = 16.px
    }

    val chipMarginCss by ListStyles.css {
      margin(4.px)
    }
  }

  class State(
    var model: ResearchList.Model,
  ) : RState
}