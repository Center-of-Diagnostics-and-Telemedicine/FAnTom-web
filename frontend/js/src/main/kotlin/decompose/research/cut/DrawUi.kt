package decompose.research.cut

import components.draw.Draw
import components.draw.Draw.Model
import decompose.Props
import decompose.research.cut.DrawUi.State
import kotlinx.css.*
import kotlinx.html.js.*
import react.*
import research.cut.*

class DrawUi(props: Props<Draw>) : CanvasUi<Draw, State, Model>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.subscribe { updateCanvas(it) }
  }

  override fun getCanvasName(): String = "draw_canvas_${state.model.cutType}"
  override fun getCanvasZIndex(): Int = 2

  class State(var model: Model) : RState
}