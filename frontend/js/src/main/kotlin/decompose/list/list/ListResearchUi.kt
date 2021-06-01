package decompose.list.list

import com.ccfraser.muirwik.components.*
import components.list.ResearchList
import decompose.RenderableComponent
import decompose.list.list.ListResearchUi.State
import kotlinx.css.*
import list.*
import react.RBuilder
import react.RState
import styled.styledDiv

class ListResearchUi(props: Props<ResearchList>) : RenderableComponent<ResearchList, State>(
  props = props,
  initialState = State(model = props.component.models.value)
) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      mTypography { +"this is research list ui" }
    }
  }


  class State(
    var model: ResearchList.Model,
  ) : RState
}