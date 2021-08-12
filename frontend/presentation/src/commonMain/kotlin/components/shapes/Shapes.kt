package components.shapes

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.Observable
import components.models.shape.ScreenShape
import components.shapes.Shapes.Dependencies
import model.*
import repository.MyMarksRepository

interface Shapes {

  val model: Value<Model>

  data class Model(
    val sliceNumber: SliceNumberModel,
    val position: PointPosition?,
    val shapes: List<ScreenShape>,
    val hounsfield: Int?,
    val cutType: CutType,
    val plane: Plane,
    val screenDimensionsModel: ScreenDimensionsModel,
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val marksRepository: MyMarksRepository
    val shapesOutput: Consumer<Output>
    val shapesInput: Observable<Input>
    val plane: Plane
    val researchId: Int
  }

  sealed class Output {
  }

  sealed class Input {
    data class Shapes(val shapes: List<Shape>) : Input()
    data class MousePosition(val position: PointPosition): Input()
    data class ScreenDimensionsChanged(val dimensions: ScreenDimensionsModel) : Input()
  }
}

@Suppress("FunctionName") // Factory function
fun Shapes(componentContext: ComponentContext, dependencies: Dependencies): Shapes =
  ShapesComponent(componentContext, dependencies)