package components.root

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.login.Login
import components.mainframe.MainFrame
import components.root.MyRoot.Child

internal class MyRootComponent(
  componentContext: ComponentContext,
  private val login: (ComponentContext, Consumer<Login.Output>) -> Login,
  private val mainFrame: (ComponentContext, Consumer<MainFrame.Output>) -> MainFrame,
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
      is Configuration.Login -> Child.Login(login(componentContext, Consumer(::onLoginOutput)))
      is Configuration.Main -> Child.Main(mainFrame(componentContext, Consumer(::onMainOutput)))
    }

  private fun onMainOutput(output: MainFrame.Output): Unit =
    when (output) {
      MainFrame.Output.Unauthorized -> {
        router.pop()
        router.push(Configuration.Login)
      }
    }

  private fun onLoginOutput(output: Login.Output): Unit =
    when (output) {
      is Login.Output.Authorized -> router.push(Configuration.Main)
    }

  private sealed class Configuration : Parcelable {
    @Parcelize
    object Login : Configuration()

    @Parcelize
    object Main : Configuration()
  }
}