package components.shapes

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.shapes.Shapes.Dependencies
import model.CutType
import model.Plane
import model.PointPosition
import model.Shape
import repository.MyMarksRepository

interface Shapes {

  val model: Value<Model>

  data class Model(
    val sliceNumber: Int,
    val position: PointPosition?,
    val shapes: List<Shape>,
    val hounsfield: Int?,
    val cutType: CutType,
    val plane: Plane
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val marksRepository: MyMarksRepository
    val shapesOutput: Consumer<Output>
    val shapesInput: Consumer<Input>
    val plane: Plane
    val researchId: Int
  }

  sealed class Output {
//    data class SliceNumberChanged(val sliceNumber: Int)
  }

  sealed class Input {
    data class Shapes(val shapes: List<Shape>) : Input()
  }
}

@Suppress("FunctionName") // Factory function
fun Shapes(componentContext: ComponentContext, dependencies: Dependencies): Shapes =
  ShapesComponent(componentContext, dependencies)