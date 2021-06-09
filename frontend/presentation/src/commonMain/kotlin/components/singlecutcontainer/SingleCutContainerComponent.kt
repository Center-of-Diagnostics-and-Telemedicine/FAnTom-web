package components.singlecutcontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.replaceCurrent
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.cut.Cut
import components.singlecutcontainer.SingleCutContainer.Child
import components.singlecutcontainer.SingleCutContainer.Dependencies
import model.CutType

class SingleCutContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cutFactory: (ComponentContext, CutType, Consumer<Cut.Output>) -> Cut,
) : SingleCutContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val router =
    router(
      initialConfiguration = ChildConfiguration(CutType.getByValue(data.modalities.keys.first())),
      handleBackButton = true,
      childFactory = ::resolveChild
    )

  override val routerState: Value<RouterState<*, Child>> = router.state

  override fun changeCutType(cutType: CutType) {
    router.replaceCurrent(ChildConfiguration(cutType))
  }

  private fun resolveChild(
    configuration: ChildConfiguration,
    componentContext: ComponentContext
  ): Child = Child(cutFactory(componentContext, configuration.cutType, Consumer(::onCutOutput)))

  private fun onCutOutput(output: Cut.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  @Parcelize
  private data class ChildConfiguration(val cutType: CutType) : Parcelable
}