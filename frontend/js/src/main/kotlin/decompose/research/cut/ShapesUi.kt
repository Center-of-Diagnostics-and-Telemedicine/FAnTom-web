package decompose.research.cut

import components.shapes.Shapes
import components.shapes.Shapes.Model
import decompose.Props
import decompose.research.cut.ShapesUi.State
import kotlinx.css.*
import react.RState

class ShapesUi(props: Props<Shapes>) : CanvasUi<Shapes, State, Model>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.subscribe { updateCanvas(it) }
  }

  override fun getCanvasName(): String = "shape_canvas_${state.model.cutType}"
  override fun getCanvasZIndex(): Int = 1

  class State(var model: Model) : RState
}