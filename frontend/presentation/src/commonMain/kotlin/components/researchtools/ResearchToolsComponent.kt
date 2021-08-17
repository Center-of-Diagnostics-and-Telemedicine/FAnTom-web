package components.researchtools

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.badoo.reaktive.base.Consumer
import components.Consumer
import components.asValue
import components.brightness.Brightness
import components.getStore
import components.grid.Grid
import components.mip.Mip
import components.researchtools.ResearchTools.Dependencies
import components.researchtools.ResearchTools.Model
import components.series.Series

internal class ResearchToolsComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies,
  mipFactory: (ComponentContext, Consumer<Mip.Output>) -> Mip,
  brightnessFactory: (ComponentContext, Consumer<Brightness.Output>) -> Brightness,
  gridFactory: (ComponentContext, Consumer<Grid.Output>) -> Grid,
  seriesFactory: (ComponentContext, Consumer<Series.Output>) -> Series,
) : ResearchTools, ComponentContext by componentContext, Dependencies by dependencies {

  private val store =
    instanceKeeper.getStore {
      ResearchToolsStoreProvider(
        storeFactory = storeFactory
      ).provide()
    }

  override val mip: Mip = mipFactory(childContext(key = "mip"), Consumer(::onMipOutput))

  override val brightness: Brightness =
    brightnessFactory(childContext(key = "brightness"), Consumer(::onBrightnessOutput))

  override val grid: Grid =
    gridFactory(childContext(key = "grid"), Consumer(::onGridOutput))

  override val series: Series =
    seriesFactory(childContext(key = "series"), Consumer(::onSeriesOutput))

  override val models: Value<Model> = store.asValue().map(stateToModel)

  override fun onBackClick() {
    toolsOutput.onNext(ResearchTools.Output.Back)
  }

  private fun onMipOutput(output: Mip.Output) {
    when (output) {
      else -> throw NotImplementedError("onMipOutput notImplemented $output")
    }
  }

  private fun onSeriesOutput(output: Series.Output) {
    when (output) {
      else -> throw NotImplementedError("onSeriesOutput notImplemented $output")
    }
  }

  private fun onGridOutput(output: Grid.Output) {
    when (output) {
      else -> throw NotImplementedError("onGridOutput notImplemented $output")
    }
  }

  private fun onBrightnessOutput(output: Brightness.Output) {
    when (output) {
      else -> throw NotImplementedError("onBrightnessOutput notImplemented $output")
    }
  }
}