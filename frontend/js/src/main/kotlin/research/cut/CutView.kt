package research.cut

import com.ccfraser.muirwik.components.MCircularProgressColor
import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.size
import com.ccfraser.muirwik.components.spacingUnits
import kotlinx.css.*
import kotlinx.css.properties.scaleY
import react.RBuilder
import styled.css
import styled.styledImg

fun RBuilder.cutView(w: Int, h: Int, slice: String, reversed: Boolean? = false, loading: Boolean) {
  if (loading) {
    mCircularProgress(color = MCircularProgressColor.secondary) {
      attrs.size = 50.px
      css {
        position = Position.absolute
        top = (h / 2).px
        left = (w / 2).px
        marginTop = (-25).px
        marginLeft = (-25).px
      }
    }
  } else {
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
}