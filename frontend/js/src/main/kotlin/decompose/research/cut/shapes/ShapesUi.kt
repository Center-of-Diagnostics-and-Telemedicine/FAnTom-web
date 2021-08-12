package decompose.research.cut.shapes

import components.shapes.Shapes
import components.shapes.Shapes.Model
import decompose.Props
import decompose.research.cut.CanvasUi
import decompose.research.cut.shapes.ShapesUi.State
import kotlinx.css.*
import kotlinx.html.classes
import react.RBuilder
import react.RState
import react.dom.attrs
import styled.css
import styled.styledCanvas
import styled.styledDiv

class ShapesUi(props: Props<Shapes>) : CanvasUi<Shapes, State, Model>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.bindToState {
      model = it
      updateCanvas(it)
    }
  }

  override fun RBuilder.render() {
    styledDiv {
      css {
        position = Position.absolute
        top = 0.px
        left = 0.px
        zIndex = getCanvasZIndex()
        width = 100.pct
        height = 100.pct
      }
      styledCanvas {
        css {
          marginLeft = state.model.screenDimensionsModel.left.px
          marginTop = state.model.screenDimensionsModel.top.px
        }
        this@styledCanvas.attrs {
          classes = classes + getCanvasName()
          width = state.model.screenDimensionsModel.calculatedScreenWidth.toString()
          height = state.model.screenDimensionsModel.calculatedScreenHeight.toString()
        }
      }
      state.model.position?.let { pointPosition(it) }
      sliceNumber(state.model.sliceNumber)
    }
  }

  override fun updateCanvas(model: Model) {
    super.updateCanvas(model)
    model.shapes.forEach { shape -> drawShape(shape) }
  }

  override fun getCanvasName(): String = "shape_canvas_${state.model.cutType}"
  override fun getCanvasZIndex(): Int = 1

  class State(var model: Model) : RState
}