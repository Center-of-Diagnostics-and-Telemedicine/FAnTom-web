package components.draw

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.Observable
import components.draw.Draw.Dependencies
import components.models.shape.ScreenShape
import model.*

interface Draw {

  val model: Value<Model>

  fun onMouseDown(mouseDownModel: MouseDown)
  fun onMouseMove(screenX: Double, screenY: Double)
  fun onMouseUp()
  fun onMouseOut()
  fun onMouseWheel(screenDeltaY: Double)
  fun onDoubleClick()
  fun onScreenDimensionChanged(clientHeight: Int?, clientWidth: Int?)

  data class Model(
    val shape: ScreenShape?,
    val plane: Plane,
    val cutType: CutType,
    val screenDimensionsModel: ScreenDimensionsModel,
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val drawOutput: Consumer<Output>
    val drawInput: Observable<Input>
    val plane: Plane
    val researchId: Int
  }

  sealed class Output {
    data class Circle(val circle: CircleModel) : Output()
    data class Ellipse(val ellipse: EllipseModel) : Output()
    data class Rectangle(val rectangle: RectangleModel) : Output()
  }

  sealed class Input {
//    data class SliceNumberChanged(val sliceNumber: Int)
  }
}

@Suppress("FunctionName") // Factory function
fun Draw(componentContext: ComponentContext, dependencies: Dependencies): Draw =
  DrawComponent(componentContext, dependencies)