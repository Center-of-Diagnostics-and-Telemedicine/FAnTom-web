package components.grid

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.asValue
import components.cut.Cut
import components.getStore
import components.grid.Grid.*
import components.grid.stateToModel
import model.GridType
import model.buildCuts
import model.initialSingleGrid
import store.tools.MyGridStore

class GridComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  private val cut: (ComponentContext, Consumer<Cut.Output>) -> Cut,
) : Grid, ComponentContext by componentContext, Dependencies by dependencies {

  private val store = instanceKeeper.getStore {
    GridStoreProvider(
      storeFactory = storeFactory,
      researchId = researchId,
      data = data
    ).provide()
  }

  private val router =
    router<Configuration, Child>(
      initialConfiguration = Configuration.Single(
        initialSingleGrid(data.type).buildCuts(data).first()
      ),
      handleBackButton = false,
      childFactory = ::createChild
    )

  override val model: Value<Model> = store.asValue().map(stateToModel)

  override fun changeGrid(gridType: GridType) {
    store.accept(MyGridStore.Intent.ChangeGrid(gridType))
  }

  private fun createChild(configuration: Configuration, componentContext: ComponentContext): Child {
    val componentFactory = cut(componentContext, Consumer(::onCutOutput))
    return when (configuration) {
      is Configuration.Single -> Child.Single(
        component = componentFactory
      )
      is Configuration.TwoHorizontal -> Child.TwoHorizontal(
        leftComponent = componentFactory,
        rightComponent = componentFactory
      )
      is Configuration.TwoVertical -> Child.TwoVertical(
        topComponent = componentFactory,
        bottomComponent = componentFactory
      )
      is Configuration.Four -> Child.Four(
        topLeftComponent = componentFactory,
        topRightComponent = componentFactory,
        bottomLeftComponent = componentFactory,
        bottomRightComponent = componentFactory,
      )
    }
  }

  private fun onCutOutput(output: Cut.Output): Unit =
    when (output) {
      else -> throw NotImplementedError("onCutOutput not implemented $output")
    }

  private sealed class Configuration : Parcelable {
    @Parcelize
    data class Single(
      val cut: model.Cut
    ) : Configuration()

    @Parcelize
    data class TwoVertical(
      val leftCut: model.Cut,
      val rightCut: model.Cut,
    ) : Configuration()

    @Parcelize
    data class TwoHorizontal(
      val topCut: model.Cut,
      val bottomCut: model.Cut,
    ) : Configuration()

    @Parcelize
    data class Four(
      val topLeftCut: model.Cut,
      val topRightCut: model.Cut,
      val bottomLeftCut: model.Cut,
      val bottomRightCut: model.Cut,
    ) : Configuration()
  }

}