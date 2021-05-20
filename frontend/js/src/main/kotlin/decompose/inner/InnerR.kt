package decompose.inner


import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.MButtonVariant
import com.ccfraser.muirwik.components.button.mButton
import decompose.RenderableComponent
import decompose.counter.CounterR
import decompose.inner.InnerR.State
import decompose.renderableChild
import react.RBuilder
import react.RState
import react.dom.br
import styled.styledDiv

class InnerR(props: Props<CounterInner>) : RenderableComponent<CounterInner, State>(
  props = props,
  initialState = State(
    leftRouterState = props.component.leftRouterState.value,
    rightRouterState = props.component.rightRouterState.value
  )
) {

  init {
    component.leftRouterState.bindToState { leftRouterState = it }
    component.rightRouterState.bindToState { rightRouterState = it }
  }

  override fun RBuilder.render() {
    mPaper(variant = MPaperVariant.outlined) {
      renderableChild(CounterR::class, this@InnerR.component.counter)

      br {}

      mGridContainer(justify = MGridJustify.spaceAround, spacing = MGridSpacing.spacing3) {
        mGridItem {}
        childWithButtons(
          child = state.leftRouterState.activeChild.instance,
          onNext = component::onNextLeftChild,
          onPrev = component::onPrevLeftChild
        )
        childWithButtons(
          child = state.rightRouterState.activeChild.instance,
          onNext = component::onNextRightChild,
          onPrev = component::onPrevRightChild
        )
        mGridItem {}
      }

      br {}
    }
  }

  private fun RBuilder.childWithButtons(
    child: CounterInner.Child,
    onNext: () -> Unit,
    onPrev: () -> Unit
  ) {
    mGridItem {
      styledDiv {
        mButton(
          caption = "Next counter",
          variant = MButtonVariant.contained,
          color = MColor.primary,
          onClick = { onNext() }
        )
      }

      br {}

      styledDiv {
        mButton(
          caption = "Prev counter",
          variant = MButtonVariant.contained,
          disabled = !child.isBackEnabled,
          onClick = { onPrev() }
        )
      }

      br {}

      renderableChild(CounterR::class, child.counter)
    }
  }

  class State(
    var leftRouterState: RouterState<*, CounterInner.Child>,
    var rightRouterState: RouterState<*, CounterInner.Child>
  ) : RState
}
