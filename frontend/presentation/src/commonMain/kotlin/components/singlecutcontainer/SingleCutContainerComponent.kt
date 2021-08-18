package components.singlecutcontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.replaceCurrent
import com.arkivanov.decompose.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cutcontainer.CutContainer
import components.singlecutcontainer.SingleCutContainer.Child
import components.singlecutcontainer.SingleCutContainer.Dependencies
import model.MyPlane

class SingleCutContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutContainerFactory: (ComponentContext, MyPlane, Consumer<CutContainer.Output>) -> CutContainer,
) : SingleCutContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val router =
    router(
      initialConfiguration = ChildConfiguration(grid.plane),
      handleBackButton = false,
      childFactory = ::resolveChild
    )

  override val routerState: Value<RouterState<*, Child>> = router.state

  override fun changeCutType(plane: MyPlane) {
    router.replaceCurrent(ChildConfiguration(plane))
  }

  private fun resolveChild(
    configuration: ChildConfiguration,
    componentContext: ComponentContext
  ): Child =
    Child(cutContainerFactory(componentContext, configuration.plane, Consumer(::onCutOutput)))

  private fun onCutOutput(output: CutContainer.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  @Parcelize
  private data class ChildConfiguration(val plane: MyPlane) : Parcelable
}