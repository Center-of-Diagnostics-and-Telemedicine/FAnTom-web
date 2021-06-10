package components.shapes

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.shapes.Shapes.Dependencies
import model.*

interface Shapes {

  val model: Value<Model>

//  fun onValueChange(value: Int)

  data class Model(
    val horizontalCoefficient: Double,
    val verticalCoefficient: Double,
    val sliceNumber: Int,
    val position: PointPosition?,
    val shapes: List<Shape>,
    val rects: List<Rect>,
    val hounsfield: Int?,
    val marks: List<MarkModel>,
    val expertMarks: List<MarkModel>,
    val moveRect: Rect?,
    val cutType: CutType,
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val shapesOutput: Consumer<Output>
    val researchId: Int
    val cutType: CutType
  }

  sealed class Output {
//    data class SliceNumberChanged(val sliceNumber: Int)
  }
}

@Suppress("FunctionName") // Factory function
fun Shapes(componentContext: ComponentContext, dependencies: Dependencies): Shapes =
  ShapesComponent(componentContext, dependencies)