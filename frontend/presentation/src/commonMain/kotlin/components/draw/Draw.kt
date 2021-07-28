package components.draw

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.draw.Draw.Dependencies
import model.CutType
import model.MouseDown
import model.Plane
import model.Shape

interface Draw {

  val model: Value<Model>

  fun onMouseDown(mouseDownModel: MouseDown)
  fun onMouseMove(dicomX: Double, dicomY: Double)
  fun onMouseUp()
  fun onMouseOut()
  fun onMouseWheel(dicomDeltaY: Int)
  fun onDoubleClick()

  data class Model(
    val shape: Shape?,
    val plane: Plane,
//    val startDicomX: Double,
//    val startDicomY: Double,
//    val dicomRadiusHorizontal: Double,
//    val dicomRadiusVertical: Double,
//    val isDrawingEllipse: Boolean = false,
//    val isDrawingRectangle: Boolean = false,
//    val isMoving: Boolean = false,
//    val isContrastBrightness: Boolean = false,
    val cutType: CutType,
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val drawOutput: Consumer<Output>
    val drawInput: Consumer<Input>
    val plane: Plane
    val researchId: Int
  }

  sealed class Output {
//    data class SliceNumberChanged(val sliceNumber: Int)
  }

  sealed class Input {
//    data class SliceNumberChanged(val sliceNumber: Int)
  }
}

@Suppress("FunctionName") // Factory function
fun Draw(componentContext: ComponentContext, dependencies: Dependencies): Draw =
  DrawComponent(componentContext, dependencies)