package components.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.login.Login
import components.mainframe.MainFrame
import components.root.MyRoot.Dependencies
import repository.*

interface MyRoot {

  val routerState: Value<RouterState<*, Child>>

  sealed class Child {
    data class Login(val component: components.login.Login) : Child()
    data class Main(val component: MainFrame) : Child()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchRepository: MyResearchRepository
    val brightnessRepository: MyBrightnessRepository
    val mipRepository: MyMipRepository
    val loginRepository: LoginRepository
    val marksRepository: MyMarksRepository
    val gridRepository: GridRepository
  }
}

@Suppress("FunctionName") // Factory function
fun MyRoot(componentContext: ComponentContext, dependencies: Dependencies): MyRoot =
  MyRootComponent(
    componentContext = componentContext,
    login = { childContext, output ->
      Login(
        componentContext = childContext,
        dependencies = object : Login.Dependencies, Dependencies by dependencies {
          override val loginOutput: Consumer<Login.Output> = output
        }
      )
    },
    mainFrame = { childContext, output ->
      MainFrame(
        componentContext = childContext,
        dependencies = object : MainFrame.Dependencies, Dependencies by dependencies {
          override val mainFrameOutput: Consumer<MainFrame.Output> = output
        }
      )
    },
  )