package decompose.research.cut

import com.ccfraser.muirwik.components.*
import components.cut.Cut
import components.cut.Cut.Model
import decompose.RenderableComponent
import decompose.research.cut.CutUi.State
import kotlinx.css.*
import react.RBuilder
import react.RState
import styled.css
import styled.styledDiv
import decompose.Props

class CutUi(props: Props<Cut>) : RenderableComponent<Cut, State>(
  props = props,
  initialState = State(model = props.component.model.value)
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      css {
        position = Position.relative
        zIndex = 0
        width = 100.pct
        height = 100.pct
        backgroundImage = Image("url(\"data:image/bmp;base64,${state.model.slice}\")")
        backgroundSize = "contain"
        backgroundPosition = "center"
        backgroundRepeat = BackgroundRepeat.noRepeat
      }
      if (state.model.loading) {
        mLinearProgress(color = MLinearProgressColor.secondary) {
          css {
            width = 100.pct
          }
        }
      }
    }
  }

  class State(var model: Model) : RState
}