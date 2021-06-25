package decompose.mainframe

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.*
import components.mainframe.MainFrame
import components.mainframe.MainFrame.Child
import decompose.RenderableComponent
import decompose.list.ListRootUi
import decompose.mainframe.MainFrameUi.State
import decompose.renderableChild
import decompose.research.ResearchRootUi
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.styledDiv
import decompose.Props

class MainFrameUi(props: Props<MainFrame>) : RenderableComponent<MainFrame, State>(
  props = props,
  initialState = State(routerState = props.component.routerState.value)
) {

  init {
    component.routerState.bindToState { routerState = it }
  }

  override fun RBuilder.render() {
    mCssBaseline()

    themeContext.Consumer { theme ->
      styledDiv {
        when (val child = state.routerState.activeChild.instance) {
          is Child.List -> renderableChild(ListRootUi::class, child.component)
          is Child.Research -> renderableChild(ResearchRootUi::class, child.component)
        }
      }
    }
  }

  class State(
    var routerState: RouterState<*, Child>,
  ) : RState
}