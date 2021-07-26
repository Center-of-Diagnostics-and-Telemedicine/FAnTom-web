package decompose.research.cut

import components.shapes.Shapes
import components.shapes.Shapes.Model
import decompose.Props
import decompose.RenderableComponent
import decompose.research.cut.ShapesUi.State
import kotlinx.css.*
import kotlinx.html.classes
import react.RBuilder
import react.RState
import react.dom.attrs
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
        top = 0.px
        left = 0.px
        zIndex = 1
        width = 100.pct
        height = 100.pct
      }
      styledCanvas {
        this@styledCanvas.attrs {
          classes = classes + "shape_canvas_${state.model.cutType}"
          width = 100.pct.toString()
          height = 100.pct.toString()
        }
      }
    }
  }

  class State(var model: Model) : RState
}