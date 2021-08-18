package components.fourcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cutcontainer.CutContainer
import components.fourcutscontainer.FourCutsContainer.Child
import components.fourcutscontainer.FourCutsContainer.Dependencies
import model.MyPlane

class FourCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutContainerFactory: (ComponentContext, MyPlane, Consumer<CutContainer.Output>) -> CutContainer,
) : FourCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val topLeftRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(gridModel.topLeft),
    key = "TopLeftRouter",
    childFactory = ::resolveChild
  )
  override val topLeftRouterState: Value<RouterState<*, Child>> = topLeftRouter.state

  private val topRightRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(gridModel.topRight),
    key = "TopRightRouter",
    childFactory = ::resolveChild
  )
  override val topRightRouterState: Value<RouterState<*, Child>> = topRightRouter.state

  private val bottomLeftRouter: Router<ChildConfiguration, Child> =
    router(
      initialConfiguration = ChildConfiguration(gridModel.bottomLeft),
      key = "BottomLeftRouter",
      childFactory = ::resolveChild
    )
  override val bottomLeftRouterState: Value<RouterState<*, Child>> = bottomLeftRouter.state

  private val bottomRightRouter: Router<ChildConfiguration, Child> =
    router(
      initialConfiguration = ChildConfiguration(gridModel.bottomRight),
      key = "BottomRightRouter",
      childFactory = ::resolveChild
    )
  override val bottomRightRouterState: Value<RouterState<*, Child>> = bottomRightRouter.state

  override fun changeTopLeftCutType(plane: MyPlane) {
    topLeftRouter.replaceCurrent(ChildConfiguration(plane))
  }

  override fun changeTopRightCutType(plane: MyPlane) {
    topRightRouter.replaceCurrent(ChildConfiguration(plane))
  }

  override fun changeBottomLeftCutType(plane: MyPlane) {
    bottomLeftRouter.replaceCurrent(ChildConfiguration(plane))
  }

  override fun changeBottomRightCutType(plane: MyPlane) {
    bottomRightRouter.replaceCurrent(ChildConfiguration(plane))
  }

  private fun resolveChild(
    config: ChildConfiguration,
    componentContext: ComponentContext
  ): Child = Child(
    component = cutContainerFactory(componentContext, config.plane, Consumer(::onCutOutput))
  )

  private fun onCutOutput(output: CutContainer.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  @Parcelize
  private data class ChildConfiguration(val plane: MyPlane) : Parcelable
}