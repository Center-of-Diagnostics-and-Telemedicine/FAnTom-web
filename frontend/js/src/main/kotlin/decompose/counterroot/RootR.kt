package decompose.counterroot

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import decompose.RenderableComponent
import decompose.counter.CounterR
import decompose.inner.InnerR
import decompose.renderableChild
import react.RBuilder
import decompose.counterroot.RootR.State
import react.RState
import react.dom.br
import styled.styledDiv


class RootR(props: Props<CounterRoot>) : RenderableComponent<CounterRoot, State>(
  props = props,
  initialState = State(routerState = props.component.routerState.value)
) {

  init {
    component.routerState.bindToState { routerState = it }
  }

  override fun RBuilder.render() {
    mPaper(variant = MPaperVariant.outlined) {
      renderableChild(CounterR::class, this@RootR.component.counter)

      br {}

      childWithButtons(state.routerState.activeChild.instance)

      br {}
    }
  }

  private fun RBuilder.childWithButtons(child: CounterRoot.Child) {
    mGridContainer(alignItems = MGridAlignItems.center, spacing = MGridSpacing.spacing3) {
      attrs {
        direction = MGridDirection.column
      }

      mGridItem {
        styledDiv {
          mButton(
            caption = "Next Child",
            variant = MButtonVariant.contained,
            color = MColor.primary,
            onClick = { component.onNextChild() }
          )
        }

        br {}

        styledDiv {
          mButton(
            "Prev Child",
            variant = MButtonVariant.contained,
            disabled = !child.isBackEnabled,
            onClick = { component.onPrevChild() }
          )
        }
      }

      mGridItem {
        renderableChild(InnerR::class, child.inner)
      }
    }
  }

  class State(
    var routerState: RouterState<*, CounterRoot.Child>,
  ) : RState
}