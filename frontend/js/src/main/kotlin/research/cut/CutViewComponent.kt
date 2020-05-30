package research.cut

import com.ccfraser.muirwik.components.spacingUnits
import kotlinx.css.*
import kotlinx.css.properties.scaleY
import react.RBuilder
import styled.css
import styled.styledImg

fun RBuilder.cut(src: String, w: Int, h: Int) {
  styledImg {
    css {
      transform.scaleY(-1)
      1.spacingUnits
      width = w.px
      height = h.px
      objectFit = ObjectFit.contain
    }
    if (src.isNotEmpty()) {
      attrs.src = "data:image/bmp;base64,${src}"
    }
  }
}
