package client.newmvi.slider.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable

interface SliderStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val value: Int = 0
  )

  sealed class Intent {
    data class Drag(val value: Int): Intent()
    data class NewValueFromSubscription(val value: Int): Intent()
  }
}