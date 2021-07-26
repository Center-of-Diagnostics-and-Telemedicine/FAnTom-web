package decompose.research.cut

import components.draw.Draw
import components.draw.Draw.Model
import decompose.RenderableComponent
import decompose.research.cut.DrawUi.State
import kotlinx.css.*
import kotlinx.html.classes
import kotlinx.html.js.*
import react.RBuilder
import react.RState
import react.dom.attrs
import styled.css
import styled.styledCanvas
import styled.styledDiv
import decompose.Props

class DrawUi(props: Props<Draw>) : RenderableComponent<Draw, State>(
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
        zIndex = 2
        width = 100.pct
        height = 100.pct
      }
      styledCanvas {
        this@styledCanvas.attrs {
          classes = classes + "draw_canvas_${state.model.cutType}"
          width = 100.pct.toString()
          height = 100.pct.toString()
        }
      }
    }

  }

  class State(var model: Model) : RState
}