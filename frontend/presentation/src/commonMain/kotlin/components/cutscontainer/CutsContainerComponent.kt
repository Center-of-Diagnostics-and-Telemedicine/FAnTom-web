package components.cutscontainer

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.replaceCurrent
import com.arkivanov.decompose.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.reaktive.bind
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.mapNotNull
import components.Consumer
import components.asValue
import components.cutcontainer.CutContainer
import components.cutscontainer.CutsContainer.*
import components.fourcutscontainer.FourCutsContainer
import components.getStore
import components.singlecutcontainer.SingleCutContainer
import components.threecutscontainer.ThreeCutsContainer
import components.twohorizontalcutscontainer.TwoHorizontalCutsContainer
import components.twoverticalcutscontainer.TwoVerticalCutsContainer
import model.MyNewGrid

class CutsContainerComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val singleCutContainerFactory: (ComponentContext, MyNewGrid, Consumer<SingleCutContainer.Output>) -> SingleCutContainer,
  private val twoVerticalCutsContainerFactory: (ComponentContext, MyNewGrid, Consumer<TwoVerticalCutsContainer.Output>) -> TwoVerticalCutsContainer,
  private val twoHorizontalCutsContainerFactory: (ComponentContext, MyNewGrid, Consumer<TwoHorizontalCutsContainer.Output>) -> TwoHorizontalCutsContainer,
  private val threeCutsContainerFactory: (ComponentContext, MyNewGrid, Consumer<ThreeCutsContainer.Output>) -> ThreeCutsContainer,
  private val fourCutsContainerFactory: (ComponentContext, MyNewGrid, Consumer<FourCutsContainer.Output>) -> FourCutsContainer,
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
      initialConfiguration = Configuration.None,
      handleBackButton = false,
      childFactory = ::createChild
    )

  override val routerState: Value<RouterState<*, Child>> = router.state

  override val model: Value<Model> = store.asValue().map(stateToModel)

  init {
    bind(lifecycle, BinderLifecycleMode.CREATE_DESTROY) {
      store.labels.mapNotNull(labelToChangeGrid) bindTo ::changeGrid
    }
  }

  override fun changeGrid(grid: MyNewGrid) {
    val newConfig = grid.toConfig()
    val oldConfig = router.state.value.activeChild.configuration
    if (oldConfig != newConfig) {
      router.replaceCurrent(newConfig)
    }
  }

  private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child {
    return when (configuration) {
      is Configuration.Single -> Child.Single(
        component = singleCutContainerFactory(
          componentContext,
          configuration.grid,
          Consumer(::onSingleCutContainerOutput)
        )
      )
      is Configuration.TwoVertical -> Child.TwoVertical(
        component = twoVerticalCutsContainerFactory(
          componentContext,
          configuration.grid,
          Consumer(::onTwoVerticalCutsContainerOutput)
        )
      )
      is Configuration.TwoHorizontal -> Child.TwoHorizontal(
        component = twoHorizontalCutsContainerFactory(
          componentContext,
          configuration.grid,
          Consumer(::onTwoHorizontalCutsContainerOutput)
        )
      )
      is Configuration.Three -> Child.Three(
        component = threeCutsContainerFactory(
          componentContext,
          configuration.grid,
          Consumer(::onThreeCutsContainerOutput)
        )
      )
      is Configuration.Four -> Child.Four(
        component = fourCutsContainerFactory(
          componentContext,
          configuration.grid,
          Consumer(::onFourCutsContainerOutput)
        )
      )
      is Configuration.None -> Child.None
    }
  }

  private fun MyNewGrid.toConfig(): Configuration =
    when (this) {
      is MyNewGrid.SingleGrid -> Configuration.Single(this)
      is MyNewGrid.TwoVerticalGrid -> Configuration.TwoVertical(this)
      is MyNewGrid.TwoHorizontalGrid -> Configuration.TwoHorizontal(this)
      is MyNewGrid.ThreeGrid -> Configuration.Three(this)
      is MyNewGrid.FourGrid -> Configuration.Four(this)
      is MyNewGrid.EmptyGrid -> Configuration.None
    }

  private fun onSingleCutContainerOutput(output: SingleCutContainer.Output) {}

  private fun onTwoVerticalCutsContainerOutput(output: TwoVerticalCutsContainer.Output) {}

  private fun onTwoHorizontalCutsContainerOutput(output: TwoHorizontalCutsContainer.Output) {}

  private fun onThreeCutsContainerOutput(output: ThreeCutsContainer.Output) {}

  private fun onFourCutsContainerOutput(output: FourCutsContainer.Output) {}

  private fun onCutOutput(output: CutContainer.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  private sealed class Configuration : Parcelable {
    @Parcelize
    data class Single(val grid: MyNewGrid.SingleGrid) : Configuration() {
      override fun toString(): String {
        return "Configuration.Single"
      }
    }

    @Parcelize
    data class TwoVertical(val grid: MyNewGrid.TwoVerticalGrid) : Configuration() {
      override fun toString(): String {
        return "Configuration.TwoVertical"
      }
    }

    @Parcelize
    data class TwoHorizontal(val grid: MyNewGrid.TwoHorizontalGrid) : Configuration() {
      override fun toString(): String {
        return "Configuration.TwoHorizontal"
      }
    }

    @Parcelize
    data class Three(val grid: MyNewGrid.ThreeGrid) : Configuration() {
      override fun toString(): String {
        return "Configuration.Three"
      }
    }

    @Parcelize
    data class Four(val grid: MyNewGrid.FourGrid) : Configuration() {
      override fun toString(): String {
        return "Configuration.Four"
      }
    }

    @Parcelize
    object None : Configuration()
  }
}