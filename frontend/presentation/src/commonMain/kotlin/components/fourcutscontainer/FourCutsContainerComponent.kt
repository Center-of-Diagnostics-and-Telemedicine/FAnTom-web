package components.fourcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cut.Cut
import components.fourcutscontainer.FourCutsContainer.Child
import components.fourcutscontainer.FourCutsContainer.Dependencies
import model.CutType

class FourCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutFactory: (ComponentContext, CutType, Consumer<Cut.Output>) -> Cut,
) : FourCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val topLeftRouter: Router<ChildConfiguration, Child> = routerBuilder("TopLeftRouter")
  override val topLeftRouterState: Value<RouterState<*, Child>> = topLeftRouter.state

  private val topRightRouter: Router<ChildConfiguration, Child> = routerBuilder("TopRightRouter")
  override val topRightRouterState: Value<RouterState<*, Child>> = topRightRouter.state

  private val bottomLeftRouter: Router<ChildConfiguration, Child> =
    routerBuilder("BottomLeftRouter")
  override val bottomLeftRouterState: Value<RouterState<*, Child>> = bottomLeftRouter.state

  private val bottomRightRouter: Router<ChildConfiguration, Child> =
    routerBuilder("BottomRightRouter")
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