package components.twohorizontalcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cutcontainer.CutContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Child
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer.Dependencies
import model.MyPlane

class TwoHorizontalCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutContainerFactory: (ComponentContext, MyPlane, Consumer<CutContainer.Output>) -> CutContainer,
) : TwoHorizontalCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val leftRouter: Router<ChildConfiguration, Child> =
    router(
      initialConfiguration = ChildConfiguration(gridModel.left),
      key = "LeftRouter",
      childFactory = ::resolveChild
    )
  override val leftRouterState: Value<RouterState<*, Child>> = leftRouter.state

  private val rightRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(gridModel.right),
    key = "RightRouter",
    childFactory = ::resolveChild
  )
  override val rightRouterState: Value<RouterState<*, Child>> = rightRouter.state

  override fun changeLeftCutType(plane: MyPlane) {
    leftRouter.replaceCurrent(ChildConfiguration(plane))
  }

  override fun changeRightCutType(plane: MyPlane) {
    rightRouter.replaceCurrent(ChildConfiguration(plane))
  }

  private fun resolveChild(
    config: ChildConfiguration,
    componentContext: ComponentContext
  ): Child =
    Child(
      component = cutContainerFactory(
        componentContext,
        config.plane,
        Consumer(::onCutOutput)
      )
    )

  private fun onCutOutput(output: CutContainer.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  @Parcelize
  private data class ChildConfiguration(val plane: MyPlane) : Parcelable
}