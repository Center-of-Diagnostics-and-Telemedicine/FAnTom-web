package components.twohorizontalcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cut.Cut
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Child
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Dependencies
import model.CutType

class TwoHorizontalCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutFactory: (ComponentContext, CutType, Consumer<Cut.Output>) -> Cut,
) : TwoHorizontalCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val leftRouter: Router<ChildConfiguration, Child> = routerBuilder("LeftRouter")
  override val leftRouterState: Value<RouterState<*, Child>> = leftRouter.state

  private val rightRouter: Router<ChildConfiguration, Child> = routerBuilder("RightRouter")
  override val rightRouterState: Value<RouterState<*, Child>> = rightRouter.state

  override fun changeLeftCutType(cutType: CutType) {
    leftRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  override fun changeRightCutType(cutType: CutType) {
    rightRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  private fun routerBuilder(key: String) = router(
    initialConfiguration = ChildConfiguration(CutType.EMPTY),
    key = key,
    childFactory = ::resolveChild
  )

  private fun resolveChild(
    config: ChildConfiguration,
    componentContext: ComponentContext
  ): Child =
    Child(component = cutFactory(componentContext, config.cutType, Consumer(::onCutOutput)))

  private fun onCutOutput(output: Cut.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  @Parcelize
  private data class ChildConfiguration(val cutType: CutType) : Parcelable
}