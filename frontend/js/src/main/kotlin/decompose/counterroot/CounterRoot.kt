package decompose.counterroot

import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.value.Value
import decompose.counter.Counter
import decompose.inner.CounterInner

interface CounterRoot {

  val counter: Counter
  val routerState: Value<RouterState<*, Child>>

  fun onNextChild()
  fun onPrevChild()

  class Child(
    val inner: CounterInner,
    val isBackEnabled: Boolean
  )
}
