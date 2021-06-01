package decompose.research

import com.ccfraser.muirwik.components.mCssBaseline
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.themeContext
import components.research.Research
import components.research.Research.*
import decompose.RenderableComponent
import decompose.research.ResearchUi.State
import react.RBuilder
import react.RState
import styled.styledDiv

class ResearchUi(props: Props<Research>) : RenderableComponent<Research, State>(
  props = props,
  initialState = State(model = props.component.models.value)
) {

  private val drawerWidth = 240

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    mCssBaseline()

    themeContext.Consumer { theme ->
      styledDiv {
        mTypography { +"this is research screen" }
      }
    }
  }

  class State(
    var model: Model
  ) : RState
}