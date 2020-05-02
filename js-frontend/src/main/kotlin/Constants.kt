import kotlinx.html.MAIN
import kotlinx.html.attributesMapOf
import react.RBuilder
import react.ReactElement
import react.dom.RDOMBuilder
import react.dom.tag

object Constants {
  const val form_group = "form-group"
  const val form_control = "form-control input-lg"
  const val RESEARCHES = "researches"
  const val ZERO = .0
}

inline fun RBuilder.main(
  classes: String? = null,
  block: RDOMBuilder<MAIN>.() -> Unit
): ReactElement = tag(block) { MAIN(attributesMapOf("class", classes), it) }

const val LEFT_MOUSE_BUTTON: Short = 0
const val MIDDLE_MOUSE_BUTTON: Short = 1
const val RIGHT_MOUSE_BUTTON: Short = 2

enum class HOVER{
  HOVER1,
  HOVER2,
  HOVER3,
  HOVER4
}