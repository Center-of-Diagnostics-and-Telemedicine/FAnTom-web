package presentation.app

import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.Colors
import com.ccfraser.muirwik.components.mThemeProvider
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createMuiTheme
import debugLog
import presentation.navigator.Screen
import presentation.screen.login
import presentation.screen.research
import presentation.screen.researchList
import react.*

abstract class App : RComponent<RProps, AppState>() {

  private var themeColor = "dark"

  init {
    state = AppState()
  }

  override fun RBuilder.render() {
    mCssBaseline()
    @Suppress("UnsafeCastFromDynamic")
    val themeOptions: ThemeOptions = js("({palette: { type: 'placeholder', primary: {main: 'placeholder'}}})")
    themeOptions.palette?.type = themeColor
    themeOptions.palette?.primary.main = Colors.Pink.shade500.toString()
    themeOptions.spacing = 1
    mThemeProvider(createMuiTheme(themeOptions)) {
      debugLog("state: ${state.screen}")
      when (state.screen) {
        Screen.AUTH -> login {
          setState { screen = Screen.RESEARCH_LIST }
        }
        Screen.RESEARCH_LIST -> researchList {
          setState {
            screen = Screen.RESEARCH
            researchId = it
          }
        }
        Screen.RESEARCH -> research(state.researchId) {
          setState {
            screen = Screen.RESEARCH_LIST
            researchId = -1
          }
        }
      }
    }

  }
}

class AppState : RState {
  var screen: Screen = Screen.AUTH
  var researchId: Int = -1
}

fun RBuilder.app() = child(App::class) {}