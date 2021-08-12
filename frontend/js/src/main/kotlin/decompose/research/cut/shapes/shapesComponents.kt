package decompose.research.cut.shapes

import com.ccfraser.muirwik.components.mTypography
import com.ccfraser.muirwik.components.spacingUnits
import kotlinx.css.*
import kotlinx.css.Position
import model.*
import react.RBuilder
import styled.css
import styled.styledDiv
import kotlin.math.roundToInt

internal fun RBuilder.pointPosition(pointPosition: PointPosition) {
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
        is PointPositionModel -> {
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

internal fun RBuilder.sliceNumber(sliceNumberModel: SliceNumberModel) {
  styledDiv {
    css {
      position = Position.absolute
      zIndex = 1
      top = 0.px
      right = 0.px
      padding(1.spacingUnits)
    }
    if (sliceNumberModel.value != 0) {
      mTypography(text = "Срез: ${sliceNumberModel.value}/${sliceNumberModel.maxValue}") {
        css { color = Color.white }
      }
    }
  }
}