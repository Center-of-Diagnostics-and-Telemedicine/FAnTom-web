package research

import kotlinx.css.LinearDimension
import kotlinx.css.marginLeft
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.mainContent(
  margiLeft: LinearDimension,
  block: RBuilder.() -> Unit
) {
  styledDiv {
    css(ResearchScreen.ResearchStyles.mainContentContainerStyle)
    css {
      marginLeft = margiLeft
//            marginRight = if (state.rightDrawerOpen) 350.px else 7.spacingUnits
    }

    block()
  }
}
