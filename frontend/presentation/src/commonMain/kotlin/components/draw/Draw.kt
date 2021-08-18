package components.draw

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.observable.Observable
import components.draw.Draw.Dependencies
import components.models.shape.ScreenShape
import model.*
import repository.MyMarksRepository

interface Draw {

  val model: Value<Model>

  fun onMouseDown(mouseDownModel: MouseDown)
  fun onMouseMove(screenX: Double, screenY: Double)
  fun onMouseUp()
  fun onMouseOut()
  fun onMouseWheel(screenDeltaY: Double)
  fun onDoubleClick()

  data class Model(
    val shape: ScreenShape?,
    val plane: MyPlane,
    val screenDimensionsModel: ScreenDimensionsModel,
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val drawOutput: Consumer<Output>
    val drawInput: Observable<Input>
    val plane: MyPlane
    val researchId: Int
    val marksRepository: MyMarksRepository
  }

  sealed class Output {
    data class Circle(val circle: CircleModel) : Output()
    data class Ellipse(val ellipse: EllipseModel) : Output()
    data class Rectangle(val rectangle: RectangleModel) : Output()
    data class ChangeSlice(val deltaDicomY: Int) : Output()
    data class ChangeContrastBrightness(val deltaX: Double, val deltaY: Double) : Output()
    data class PointPosition(val pointPosition: PointPositionModel?) : Output()
    object OpenFullCut : Output()
  }

  sealed class Input {
    data class ScreenDimensionsChanged(val dimensions: ScreenDimensionsModel) : Input()
  }
}

@Suppress("FunctionName") // Factory function
fun Draw(componentContext: ComponentContext, dependencies: Dependencies): Draw =
  DrawComponent(componentContext, dependencies)