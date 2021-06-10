package components.cutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.replaceCurrent
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.decompose.value.operator.map
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.asValue
import components.cutcontainer.CutContainer
import components.cutscontainer.CutsContainer.*
import components.fourcutscontainer.FourCutsContainer
import components.getStore
import components.singlecutcontainer.SingleCutContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer
import model.GridType

class CutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val singleCutContainerFactory: (ComponentContext, Consumer<SingleCutContainer.Output>) -> SingleCutContainer,
  private val twoVerticalCutsContainerFactory: (ComponentContext, Consumer<TwoVerticalCutsContainer.Output>) -> TwoVerticalCutsContainer,
  private val twoHorizontalCutsContainerFactory: (ComponentContext, Consumer<TwoHorizontalCutsContainer.Output>) -> TwoHorizontalCutsContainer,
  private val fourCutsContainerFactory: (ComponentContext, Consumer<FourCutsContainer.Output>) -> FourCutsContainer,
) : CutsContainer, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    CutsContainerStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      researchRepository = researchRepository,
      gridRepository = gridRepository,
      data = data
    ).provide()
  }

  private val router =
    router<Configuration, Child>(
      initialConfiguration = Configuration.Four,
      handleBackButton = false,
      childFactory = ::createChild
    )

  override val routerState: Value<RouterState<*, Child>> = router.state

  override val model: Value<Model> = store.asValue().map(stateToModel)

  init {
    store.asValue().observe(lifecycle) {
      changeGrid(it.gridType)
    }
  }

  override fun changeGrid(gridType: GridType) {
    router.replaceCurrent(gridType.toConfig())
  }

  private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child {
    return when (configuration) {
      is Configuration.Single -> Child.Single(
        component = singleCutContainerFactory(
          componentContext,
          Consumer(::onSingleCutContainerOutput)
        )
      )
      Configuration.TwoVertical -> Child.TwoVertical(
        component = twoVerticalCutsContainerFactory(
          componentContext,
          Consumer(::onTwoVerticalCutsContainerOutput)
        )
      )
      Configuration.TwoHorizontal -> Child.TwoHorizontal(
        component = twoHorizontalCutsContainerFactory(
          componentContext,
          Consumer(::onTwoHorizontalCutsContainerOutput)
        )
      )
      Configuration.Four -> Child.Four(
        component = fourCutsContainerFactory(
          componentContext,
          Consumer(::onFourCutsContainerOutput)
        )
      )
    }
  }

  private fun GridType.toConfig(): Configuration = when (this) {
    GridType.Single -> Configuration.Single
    GridType.TwoVertical -> Configuration.TwoVertical
    GridType.TwoHorizontal -> Configuration.TwoHorizontal
    GridType.Four -> Configuration.Four
  }

  private fun onSingleCutContainerOutput(output: SingleCutContainer.Output) {

  }

  private fun onTwoVerticalCutsContainerOutput(output: TwoVerticalCutsContainer.Output) {

  }

  private fun onTwoHorizontalCutsContainerOutput(output: TwoHorizontalCutsContainer.Output) {

  }

  private fun onFourCutsContainerOutput(output: FourCutsContainer.Output) {

  }

  private fun onCutOutput(output: CutContainer.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  private sealed class Configuration : Parcelable {
    @Parcelize
    object Single : Configuration() {
      override fun toString(): String {
        return "Configuration.Single"
      }
    }

    @Parcelize
    object TwoVertical : Configuration() {
      override fun toString(): String {
        return "Configuration.TwoVertical"
      }
    }

    @Parcelize
    object TwoHorizontal : Configuration() {
      override fun toString(): String {
        return "Configuration.TwoHorizontal"
      }
    }

    @Parcelize
    object Four : Configuration() {
      override fun toString(): String {
        return "Configuration.Four"
      }
    }
  }
}