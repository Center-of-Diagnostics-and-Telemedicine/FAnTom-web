package components.twoverticalcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cutcontainer.CutContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Child
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Dependencies
import model.CutType
import model.initialTwoVerticalGrid

class TwoVerticalCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutContainerFactory: (ComponentContext, CutType, Consumer<CutContainer.Output>) -> CutContainer,
) : TwoVerticalCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  val grid = initialTwoVerticalGrid(data.type)

  private val topRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(grid.top),
    key = "TopRouter",
    childFactory = ::resolveChild
  )
  override val topRouterState: Value<RouterState<*, Child>> = topRouter.state

  private val bottomRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(grid.bottom),
    key = "BottomRouter",
    childFactory = ::resolveChild
  )
  override val bottomRouterState: Value<RouterState<*, Child>> = bottomRouter.state

  override fun changeTopCutType(cutType: CutType) {
    topRouter.replaceCurrent(ChildConfiguration(cutType))
  }

  override fun changeBottomCutType(cutType: CutType) {
    bottomRouter.replaceCurrent(ChildConfiguration(cutType))
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


