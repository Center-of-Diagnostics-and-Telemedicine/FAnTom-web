package root

import ScreenType
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.mThemeProvider
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createMuiTheme
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

abstract class App : RComponent<AppProps, AppState>() {

  private var themeColor = "light"

  init {
    state = AppState(
      screen = ScreenType.AUTH,
      researchId = -1
    )
  }

  override fun RBuilder.render() {
    mCssBaseline()
    @Suppress("UnsafeCastFromDynamic")
    val themeOptions: ThemeOptions =
      js("({palette: {primary: {light: '#757ce8',main: '#3f50b5',dark: '#002884',contrastText:'#fff',},secondary: {light: '#ff7961',main: '#f44336',dark:'#ba000d',contrastText: '#000',},}})")
    themeOptions.palette?.type = themeColor
    themeOptions.typography?.fontSize = 12
    themeOptions.spacing = 1

    mThemeProvider(createMuiTheme(themeOptions)) {
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
          dependencies = object : ResearchScreen.Dependencies, Dependencies by props.dependencies {
            override val researchId: Int = state.researchId
          }
        )
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
  var researchId: Int
) : RState

interface AppProps : RProps {
  var dependencies: App.Dependencies
}

fun RBuilder.app(dependencies: App.Dependencies) = child(App::class) {
  attrs.dependencies = dependencies
}
