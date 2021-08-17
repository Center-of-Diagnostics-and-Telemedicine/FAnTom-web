package decompose.research.tools

import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.transitions.mCollapse
import components.series.Series
import decompose.Props
import decompose.RenderableComponent
import decompose.research.tools.SeriesUi.State
import kotlinx.css.paddingLeft
import model.SeriesModel
import react.RBuilder
import react.RState
import styled.css
import styled.styledDiv

class SeriesUi(props: Props<Series>) : RenderableComponent<Series, State>(
  props = props,
  initialState = State(
    model = props.component.model.value,
    open = true
  )
) {

  init {
    component.model.bindToState { model = it }
  }

  override fun RBuilder.render() {
    styledDiv {
      mCollapse(show = state.open) {
        mList {
          css { paddingLeft = 4.spacingUnits }
          state.model.series.forEach { entry: Map.Entry<String, SeriesModel> ->
            mListItem(
              primaryText = entry.value.name,
              onClick = { component.changeSeries(entry.key) },
              selected = entry.value.name == state.model.currentSeries.name
            )
          }
        }
      }
    }
  }

  class State(
    var model: Series.Model,
    var open: Boolean
  ) : RState
}