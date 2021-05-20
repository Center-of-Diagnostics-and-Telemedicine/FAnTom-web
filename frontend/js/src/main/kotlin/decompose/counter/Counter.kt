package decompose.counter

import com.arkivanov.decompose.value.Value

interface Counter {

  val model: Value<Model>

  class Model(
    val text: String
  )
}
