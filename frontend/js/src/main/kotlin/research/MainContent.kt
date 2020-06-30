package research

import kotlinx.css.LinearDimension
import kotlinx.css.marginLeft
import kotlinx.css.marginRight
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.mainContent(
  marginnLeft: LinearDimension,
  marginnRight: LinearDimension,
  block: RBuilder.() -> Unit
) {
  styledDiv {
    css(ResearchScreen.ResearchStyles.mainContentContainerStyle)
    css {
      marginLeft = marginnLeft
      marginRight = marginnRight
    }

    block()
  }
}
