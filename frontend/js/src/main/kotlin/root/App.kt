package root

import ScreenType
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.mThemeProvider
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.styles.ThemeOptions
import com.ccfraser.muirwik.components.styles.createMuiTheme
import controller.ListController
import controller.LoginController
import kotlinx.css.*
import list.ListScreen
import list.list
import login.LoginScreen
import login.login
import react.*
import repository.LoginRepository
import repository.ResearchRepository
import styled.StyleSheet

abstract class App : RComponent<AppProps, AppState>() {

  private var themeColor = "light"

  init {
    state = AppState(
      screen = ScreenType.AUTH,
      currentResearchAccessionName = ""
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
            override val output: (LoginController.Output) -> Unit = ::loginOutput
          }
        )
        ScreenType.LIST -> list(
          dependencies = object : ListScreen.Dependencies, Dependencies by props.dependencies {
            override val output: (ListController.Output) -> Unit = ::listOutput
          }
        )
        ScreenType.RESEARCH -> {
        }
      }
    }
  }

  private fun listOutput(output: ListController.Output) {
    when (output) {
      is ListController.Output.ItemSelected ->
        setState {
          debugLog("output in app")
          currentResearchAccessionName = output.id
        }
    }
  }

  private fun loginOutput(output: LoginController.Output) {
    when (output) {
      LoginController.Output.Authorized -> setState { screen = ScreenType.LIST }
    }
  }

  object TodoStyles : StyleSheet("TodoStyles", isStatic = true) {
    val addCss by css {
      display = Display.inlineFlex
      width = 100.pct
      padding(2.spacingUnits)
    }

    val listCss by css {
      width = 100.pct
    }

    val columnCss by css {
      display = Display.flex
      flexDirection = FlexDirection.column
      alignItems = Align.center
    }

    val headerMarginCss by css {
      marginTop = 9.spacingUnits
    }

    val detailsButtonsCss by css {
      display = Display.flex
      flexDirection = FlexDirection.row
      justifyContent = JustifyContent.spaceBetween
      alignItems = Align.center
      width = 100.pct
      paddingLeft = 2.spacingUnits
    }

    val detailsInputCss by css {
      width = 100.pct
      padding(2.spacingUnits)
    }
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val loginRepository: LoginRepository
    val researchRepository: ResearchRepository
  }
}

class AppState(
  var screen: ScreenType,
  var currentResearchAccessionName: String
) : RState

interface AppProps : RProps {
  var dependencies: App.Dependencies
}

fun RBuilder.app(dependencies: App.Dependencies) = child(App::class) {
  attrs.dependencies = dependencies
}
