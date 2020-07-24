package research.cut

import com.ccfraser.muirwik.components.MLinearProgressColor
import com.ccfraser.muirwik.components.mLinearProgress
import com.ccfraser.muirwik.components.spacingUnits
import kotlinx.css.*
import kotlinx.css.properties.scaleY
import react.RBuilder
import styled.css
import styled.styledImg

fun RBuilder.cutView(
  w: Int,
  h: Int,
  slice: String,
  reversed: Boolean? = false,
  loading: Boolean
) {
//  if (loading) {
//    mLinearProgress(color = MLinearProgressColor.secondary) {
//      css {
//        width = w.px
//        top = 0.px
//        left = 0.px
//      }
//    }
//  }
  styledImg {
    css {
      if (reversed == true) transform.scaleY(-1)
      1.spacingUnits
      width = w.px
      height = h.px
      objectFit = ObjectFit.contain
    }
    attrs.src = "data:image/bmp;base64,${slice}"
  }
}