package decompose

import react.RBuilder
import kotlin.reflect.KClass

fun <M : Any, T : RenderableComponent<M, *>> RBuilder.renderableChild(clazz: KClass<out T>, model: M) {
  child(clazz) {
    key = model.uniqueId().toString()
    attrs.component = model
  }
}