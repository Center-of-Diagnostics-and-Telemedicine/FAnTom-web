package presentation.app

import client.domain.repository.LoginRepository
import client.newmvi.newlogin.controller.LoginController
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createMuiTheme
import debugLog
import presentation.navigator.Screen
import presentation.screen.newLogin.newLogin
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
        Screen.AUTH -> newLogin {
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