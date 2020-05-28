package components

import com.ccfraser.muirwik.components.mCircularProgress
import com.ccfraser.muirwik.components.size
import kotlinx.css.*
import react.RBuilder
import styled.css

fun RBuilder.loading(isLoading: Boolean) = if (isLoading) {
  mCircularProgress {
    attrs.size = 80.px
    css {
      position = Position.absolute
      top = 50.pct
      left = 50.pct
      marginTop = (-40).px
      marginLeft = (-40).px
    }
  }
} else {
  EmptyElement
}
