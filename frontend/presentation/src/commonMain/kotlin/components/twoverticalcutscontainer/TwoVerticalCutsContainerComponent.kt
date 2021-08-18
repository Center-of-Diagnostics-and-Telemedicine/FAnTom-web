package components.twoverticalcutscontainer

import com.arkivanov.decompose.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cutcontainer.CutContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Child
import components.twoverticalcutscontainer.TwoVerticalCutsContainer.Dependencies
import model.MyPlane

class TwoVerticalCutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutContainerFactory: (ComponentContext, MyPlane, Consumer<CutContainer.Output>) -> CutContainer,
) : TwoVerticalCutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val topRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(gridModel.top),
    key = "TopRouter",
    childFactory = ::resolveChild
  )
  override val topRouterState: Value<RouterState<*, Child>> = topRouter.state

  private val bottomRouter: Router<ChildConfiguration, Child> = router(
    initialConfiguration = ChildConfiguration(gridModel.bottom),
    key = "BottomRouter",
    childFactory = ::resolveChild
  )
  override val bottomRouterState: Value<RouterState<*, Child>> = bottomRouter.state

  override fun changeTopCutType(plane: MyPlane) {
    topRouter.replaceCurrent(ChildConfiguration(plane))
  }

  override fun changeBottomCutType(plane: MyPlane) {
    bottomRouter.replaceCurrent(ChildConfiguration(plane))
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


