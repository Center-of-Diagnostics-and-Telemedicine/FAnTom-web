package components.fourcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cutcontainer.CutContainer
import components.fourcutscontainer.FourCutsContainer.Child
import components.fourcutscontainer.FourCutsContainer.Dependencies
import model.CutType
import model.initialFourGrid

class FourCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutContainerFactory: (ComponentContext, CutType, Consumer<CutContainer.Output>) -> CutContainer,
) : FourCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  val grid = initialFourGrid(data.type)

  private val topLeftRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(grid.topLeft),
    key = "TopLeftRouter",
    childFactory = ::resolveChild
  )
  override val topLeftRouterState: Value<RouterState<*, Child>> = topLeftRouter.state

  private val topRightRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(grid.topRight),
    key = "TopRightRouter",
    childFactory = ::resolveChild
  )
  override val topRightRouterState: Value<RouterState<*, Child>> = topRightRouter.state

  private val bottomLeftRouter: Router<ChildConfiguration, Child> =
    router(
      initialConfiguration = ChildConfiguration(grid.bottomLeft),
      key = "BottomLeftRouter",
      childFactory = ::resolveChild
    )
  override val bottomLeftRouterState: Value<RouterState<*, Child>> = bottomLeftRouter.state

  private val bottomRightRouter: Router<ChildConfiguration, Child> =
    router(
      initialConfiguration = ChildConfiguration(grid.bottomRight),
      key = "BottomRightRouter",
      childFactory = ::resolveChild
    )
  override val bottomRightRouterState: Value<RouterState<*, Child>> = bottomRightRouter.state

  override fun changeTopLeftCutType(cutType: CutType) {
    topLeftRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  override fun changeTopRightCutType(cutType: CutType) {
    topRightRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  override fun changeBottomLeftCutType(cutType: CutType) {
    bottomLeftRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  override fun changeBottomRightCutType(cutType: CutType) {
    bottomRightRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  private fun resolveChild(
    config: ChildConfiguration,
    componentContext: ComponentContext
  ): Child = Child(
    component = cutContainerFactory(componentContext, config.cutType, Consumer(::onCutOutput))
  )

  private fun onCutOutput(output: CutContainer.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  @Parcelize
  private data class ChildConfiguration(val cutType: CutType) : Parcelable
}