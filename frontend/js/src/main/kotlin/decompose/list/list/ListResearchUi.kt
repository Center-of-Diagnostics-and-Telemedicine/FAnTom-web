package decompose.list.list

import com.ccfraser.muirwik.components.*
import components.list.ResearchList
import decompose.RenderableComponent
import decompose.list.list.ListResearchUi.State
import kotlinx.css.*
import list.*
import react.RBuilder
import react.RState
import decompose.Props

class ListResearchUi(props: Props<ResearchList>) : RenderableComponent<ResearchList, State>(
  props = props,
  initialState = State(model = props.component.models.value)
) {

  init {
    component.models.bindToState { model = it }
  }

  override fun RBuilder.render() {
    researchList(state.model.items, onClick = { component.onItemClick(it.id) })
  }


  class State(
    var model: ResearchList.Model,
  ) : RState
}