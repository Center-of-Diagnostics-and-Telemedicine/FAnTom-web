package decompose.myroot

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.list.ResearchList
import components.login.Login
import decompose.myroot.MyRoot.Child

internal class MyRootComponent(
  componentContext: ComponentContext,
  private val login: (ComponentContext, Consumer<Login.Output>) -> Login,
  private val list: (ComponentContext, Consumer<ResearchList.Output>) -> ResearchList
) : MyRoot, ComponentContext by componentContext {

  private val router =
    router<Configuration, Child>(
      initialConfiguration = Configuration.Login,
      handleBackButton = true,
      childFactory = ::createChild
    )

  override val routerState: Value<RouterState<*, Child>> = router.state

  private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child =
    when (configuration) {
      is Configuration.Login -> Child.Login(login(componentContext, Consumer(::onMainOutput)))
      is Configuration.List -> Child.List(list(componentContext, Consumer(::onEditOutput)))
      is Configuration.Research -> TODO()
    }

  private fun onMainOutput(output: Login.Output): Unit =
    when (output) {
      Login.Output.Authorized -> router.push(Configuration.List)
    }

  private fun onEditOutput(output: ResearchList.Output): Unit =
    when (output) {
      is ResearchList.Output.ItemSelected -> router.push(Configuration.Research(output.researchId))
    }

  private sealed class Configuration : Parcelable {
    //    @Parcelize
    object Login : Configuration()

    //    @Parcelize
    object List : Configuration()

    //    @Parcelize
    data class Research(val researchId: Int) : Configuration()
  }
}