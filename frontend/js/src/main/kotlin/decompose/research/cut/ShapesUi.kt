package decompose.research.cut

import components.shapes.Shapes
import components.shapes.Shapes.Model
import decompose.RenderableComponent
import decompose.research.cut.ShapesUi.State
import kotlinx.css.*
import kotlinx.html.classes
import react.RBuilder
import react.RState
import styled.css
import styled.styledCanvas
import styled.styledDiv

class ShapesUi(props: Props<Shapes>) : RenderableComponent<Shapes, State>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      css {
        position = Position.absolute
        zIndex = 1
        top = 0.px
        left = 0.px
      }
      styledCanvas {
        attrs {
          classes = classes + "shape_canvas_${state.model.cutType}"
          width = 100.pct.toString()
          height = 100.pct.toString()
        }
      }
    }
  }

  class State(var model: Model) : RState
}