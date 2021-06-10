package components.twoverticalcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cutcontainer.CutContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Child
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Dependencies
import model.CutType

class TwoVerticalCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutContainerFactory: (ComponentContext, CutType, Consumer<CutContainer.Output>) -> CutContainer,
) : TwoVerticalCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val topRouter: Router<ChildConfiguration, Child> = routerBuilder("TopRouter")
  override val topRouterState: Value<RouterState<*, Child>> = topRouter.state

  private val bottomRouter: Router<ChildConfiguration, Child> = routerBuilder("BottomRouter")
  override val bottomRouterState: Value<RouterState<*, Child>> = bottomRouter.state

  override fun changeTopCutType(cutType: CutType) {
    topRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  override fun changeBottomCutType(cutType: CutType) {
    bottomRouter.replaceCurrent(ChildConfiguration(cutType))
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
    Child(component = cutContainerFactory(componentContext, config.cutType, Consumer(::onCutOutput)))

  private fun onCutOutput(output: CutContainer.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  @Parcelize
  private data class ChildConfiguration(val cutType: CutType) : Parcelable
}


