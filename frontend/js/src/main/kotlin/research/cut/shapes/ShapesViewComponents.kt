package research.cut.shapes

import com.ccfraser.muirwik.components.list.MListItemAlignItems
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.mClickAwayListener
import com.ccfraser.muirwik.components.mIcon
import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.spacingUnits
import com.ccfraser.muirwik.components.transitions.mCollapse
import kotlinx.css.*
import kotlinx.css.Position
import model.*
import react.RBuilder
import styled.css
import styled.styledDiv
import kotlin.math.roundToInt

fun RBuilder.huValue(value: Int) {
  styledDiv {
    css {
      position = Position.absolute
      zIndex = 1
      bottom = 0.px
      right = 0.px
      padding(1.spacingUnits)
    }
    mTypography(text = "HU: $value") {
      css { color = Color.white }
    }
  }
}

fun RBuilder.brightnessValue(value: Int) {
  styledDiv {
    css {
      position = Position.absolute
      zIndex = 1
      bottom = 0.px
      right = 0.px
      padding(1.spacingUnits)
    }
    mTypography(text = "Val: $value") {
      css { color = Color.white }
    }
  }
}

fun RBuilder.sliceNumber(sliceNumber: Int, imagesCount: Int) {
  styledDiv {
    css {
      position = Position.absolute
      zIndex = 1
      top = 0.px
      right = 0.px
      padding(1.spacingUnits)
    }
    if (sliceNumber != 0 && imagesCount > 1) {
      mTypography(text = "Срез: $sliceNumber") {
        css { color = Color.white }
      }
    }
  }
}

fun RBuilder.cutType(
  cutName: String?,
  otherTypes: List<CutType>,
  onClickAway: () -> Unit,
  onClick: () -> Unit,
  showMenu: Boolean,
  onMenuItemClick: (CutType) -> Unit
) {
  if (cutName != null && otherTypes.isNotEmpty()) {
    styledDiv {
      css {
        position = Position.absolute
        zIndex = 10
        top = 0.px
        left = 0.px
        padding(1.spacingUnits)
      }
      mClickAwayListener(onClickAway = { onClickAway() }) {
        mList(disablePadding = true) {
          css {
            backgroundColor = Color("#424242")
            borderRadius = 4.px

          }
          mListItem(
            primaryText = cutName,
            onClick = { onClick() },
            divider = false
          ) {
            if (otherTypes.isNotEmpty()) {
              if (showMenu) mIcon("expand_less") else mIcon("expand_more")
            }
          }
          mCollapse(show = showMenu) {
            mList {
              otherTypes.forEach { cutType ->
                mListItem(
                  button = true,
                  onClick = { onMenuItemClick(cutType) },
                  alignItems = MListItemAlignItems.flexStart
                ) {
                  mTypography(text = cutType.name)
                }
              }
            }
          }
        }
      }
    }
  }
}

fun RBuilder.pointPosition(pointPosition: PointPosition) {
  styledDiv {
    css {
      position = Position.absolute
      zIndex = 1
      bottom = 0.px
      left = 0.px
      padding(1.spacingUnits)
    }
    pointPosition.let {
      when (it) {
        is MultiPlanarPointPosition -> {
          mTypography(text = "Сагиттальный (x): ${it.x.roundToInt()}") {
            css { color = Color(blue) }
          }
          mTypography(text = "Фронтальный (y): ${it.y.roundToInt()}") {
            css { color = Color(pink) }
          }
          mTypography(text = "Аксиальный(z): ${it.z.roundToInt()}") {
            css { color = Color(yellow) }
          }
        }
        is PlanarPointPosition -> {
          mTypography(text = "x: ${it.x.roundToInt()}") {
            css { color = Color.white }
          }
          mTypography(text = "y: ${it.y.roundToInt()}") {
            css { color = Color.white }
          }
        }
        else -> {
        }
      }
    }
  }
}