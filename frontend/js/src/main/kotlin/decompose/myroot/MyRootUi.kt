package decompose.myroot

import com.arkivanov.decompose.RouterState
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createMuiTheme
import components.root.MyRoot
import decompose.Props
import decompose.RenderableComponent
import decompose.login.LoginUi
import decompose.mainframe.MainFrameUi
import decompose.myroot.MyRootUi.State
import decompose.renderableChild
import react.RBuilder
import react.RState
import styled.styledDiv

class MyRootUi(props: Props<MyRoot>) : RenderableComponent<MyRoot, State>(
  props = props,
  initialState = State(routerState = props.component.routerState.value)
) {

  init {
    component.routerState.bindToState { routerState = it }
  }

  override fun RBuilder.render() {
    mCssBaseline()
    @Suppress("UnsafeCastFromDynamic")
    val themeOptions: ThemeOptions = js("({palette: { type: 'placeholder', primary: {main: 'placeholder'}}})")
    themeOptions.palette?.type = "dark"
    themeOptions.palette?.primary.main = Colors.Pink.shade500.toString()
    themeOptions.spacing = 1

    mThemeProvider(createMuiTheme(themeOptions)) {
      themeContext.Consumer { theme ->
        styledDiv {
          when (val child = state.routerState.activeChild.instance) {
            is MyRoot.Child.Login -> renderableChild(LoginUi::class, child.component)
            is MyRoot.Child.Main -> renderableChild(MainFrameUi::class, child.component)
          }
        }
      }
    }
  }

  class State(
    var routerState: RouterState<*, MyRoot.Child>,
  ) : RState
}