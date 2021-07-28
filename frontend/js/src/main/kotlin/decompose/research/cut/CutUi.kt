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
import root.debugLog

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
        display = Display.flex
        justifyContent = JustifyContent.center
        alignItems = Align.center
        backgroundImage = Image("url(\"data:image/bmp;base64,${state.model.slice}\")")
        backgroundSize = "contain"
        backgroundPosition = "center"
        backgroundRepeat = BackgroundRepeat.noRepeat
      }
      if (state.model.loading) {
        mCircularProgress(color = MCircularProgressColor.secondary) {
          attrs.size = 25.px
        }
      }
    }
  }

  class State(var model: Model) : RState
}