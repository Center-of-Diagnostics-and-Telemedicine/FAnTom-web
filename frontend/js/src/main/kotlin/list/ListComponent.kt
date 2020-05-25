package list

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.card.mCard
import com.ccfraser.muirwik.components.card.mCardActionArea
import com.ccfraser.muirwik.components.card.mCardContent
import com.ccfraser.muirwik.components.card.mCardHeader
import com.ccfraser.muirwik.components.styles.Breakpoint
import kotlinx.css.Color
import kotlinx.css.backgroundColor
import model.Research
import react.RBuilder
import styled.StyledElementBuilder
import styled.css

private val breakpoints =
  MGridBreakpoints(MGridSize.cells6)
    .up(Breakpoint.lg, MGridSize.cells4)
    .down(Breakpoint.sm, MGridSize.cells12)

fun RBuilder.researchList(items: List<Research>, onClick: (Int) -> Unit) {
  mGridContainer(MGridSpacing.spacing2) {
    if (items.isNotEmpty()) {
      for (research in items) {
        researchCard(breakpoints, onClick, research)
      }
    }
  }
}

private fun StyledElementBuilder<MGridProps>.researchCard(
  breakpoints: MGridBreakpoints,
  onClick: (Int) -> Unit,
  research: Research
) {
  mGridItem(breakpoints) {
    mCardActionArea(onClick = { onClick(research.id) }) {
      mCard {
        css {
          if (research.seen) {
            backgroundColor = Color.lightYellow
          }
          if (research.done) {
            backgroundColor = Color.lightGreen
          }
        }
        mCardHeader(title = research.name)
        mCardContent {
          mTypography(text = research.id.toString())
        }
      }
    }
  }
}
