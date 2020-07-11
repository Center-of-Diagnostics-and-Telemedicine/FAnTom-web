package root

import ScreenType
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.Colors
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.mThemeProvider
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createMuiTheme
import com.ccfraser.muirwik.components.themeContext
import controller.ListController
import controller.LoginController
import list.ListScreen
import list.list
import login.LoginScreen
import login.login
import react.*
import repository.LoginRepository
import repository.MarksRepository
import repository.ResearchRepository
import research.ResearchScreen
import research.research
import styled.styledDiv

abstract class App : RComponent<AppProps, AppState>() {

  init {
    state = AppState(
      screen = ScreenType.AUTH,
      researchId = -1,
      themeColor = "dark"
    )
  }

  override fun RBuilder.render() {
    mCssBaseline()
    @Suppress("UnsafeCastFromDynamic")
    val themeOptions: ThemeOptions = js("({palette: { type: 'placeholder', primary: {main: 'placeholder'}}})")
    themeOptions.palette?.type = state.themeColor
    themeOptions.palette?.primary.main = Colors.Pink.shade500.toString()
    themeOptions.spacing = 1

    mThemeProvider(createMuiTheme(themeOptions)) {
      themeContext.Consumer { theme ->
        styledDiv {
          when (state.screen) {
            ScreenType.AUTH -> login(
              dependencies = object : LoginScreen.Dependencies, Dependencies by props.dependencies {
                override val loginOutput: (LoginController.Output) -> Unit = ::loginOutput
              }
            )
            ScreenType.LIST -> list(
              dependencies = object : ListScreen.Dependencies, Dependencies by props.dependencies {
                override val listOutput: (ListController.Output) -> Unit = ::listOutput
              }
            )
            ScreenType.RESEARCH -> research(
              dependencies = object : ResearchScreen.Dependencies,
                Dependencies by props.dependencies {
                override val researchId: Int = state.researchId
              }
            )
          }

        }
      }
    }
  }

  private fun listOutput(output: ListController.Output) {
    when (output) {
      is ListController.Output.ItemSelected ->
        setState {
          researchId = output.id
          screen = ScreenType.RESEARCH
        }
    }
  }

  private fun loginOutput(output: LoginController.Output) {
    when (output) {
      LoginController.Output.Authorized -> setState { screen = ScreenType.LIST }
    }
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val loginRepository: LoginRepository
    val researchRepository: ResearchRepository
    val marksRepository: MarksRepository
  }
}

class AppState(
  var screen: ScreenType,
  var researchId: Int,
  var themeColor: String
) : RState

interface AppProps : RProps {
  var dependencies: App.Dependencies
}

fun RBuilder.app(dependencies: App.Dependencies) = child(App::class) {
  attrs.dependencies = dependencies
}
