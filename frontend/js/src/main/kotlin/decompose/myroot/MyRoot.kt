package decompose.myroot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.list.ResearchList
import components.login.Login
import decompose.myroot.MyRoot.Dependencies
import repository.LoginRepository
import repository.ResearchRepository

interface MyRoot {

  val routerState: Value<RouterState<*, Child>>

  sealed class Child {
    data class Login(val component: components.login.Login) : Child()
    data class List(val component: ResearchList) : Child()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val repository: LoginRepository
    val researchRepository: ResearchRepository
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
          override val mainOutput: Consumer<Login.Output> = output
        }
      )
    },
    list = { childContext, output ->
      ResearchList(
        componentContext = childContext,
        dependencies = object : ResearchList.Dependencies, Dependencies by dependencies {
          override val listOutput: Consumer<ResearchList.Output> = output
        }
      )
    }
  )