package components.researchtools

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.brightness.Brightness
import components.grid.Grid
import components.mip.Mip
import components.researchtools.ResearchTools.Dependencies
import model.ResearchData
import model.Tool
import repository.BrightnessRepository
import repository.GridRepository
import repository.MipRepository

interface ResearchTools {

  val grid: Grid
  val mip: Mip
  val brightness: Brightness

  //  val preset: Preset
  val models: Value<Model>

  fun onBackClick()

  data class Model(
    val list: List<Tool>,
    val current: Tool?
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val researchId: Int
    val toolsOutput: Consumer<Output>
    val mipRepository: MipRepository
    val brightnessRepository: BrightnessRepository
    val gridRepository: GridRepository
    val data: ResearchData
  }

  sealed class Output {
    object Back : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun ResearchTools(componentContext: ComponentContext, dependencies: Dependencies): ResearchTools =
  ResearchToolsComponent(
    componentContext = componentContext,
    dependencies = dependencies,
    mipFactory = { childContext, output ->
      Mip(
        componentContext = childContext,
        dependencies = object : Mip.Dependencies, Dependencies by dependencies {
          override val mipOutput: Consumer<Mip.Output> = output
        })
    },
    brightnessFactory = { childContext, output ->
      Brightness(
        componentContext = childContext,
        dependencies = object : Brightness.Dependencies, Dependencies by dependencies {
          override val brightnessOutput: Consumer<Brightness.Output> = output
        })
    },
    gridFactory = { childContext, output ->
      Grid(
        componentContext = childContext,
        dependencies = object : Grid.Dependencies, Dependencies by dependencies {
          override val gridOutput: Consumer<Grid.Output> = output
        })
    },
  )