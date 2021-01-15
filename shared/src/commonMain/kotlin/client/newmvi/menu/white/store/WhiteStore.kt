package client.newmvi.menu.white.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.INITIAL_WHITE

interface WhiteStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val value: Double = INITIAL_WHITE
  )

  sealed class Intent {
    data class Drag(val value: Double): Intent()
    data class NewValueFromSubscription(val value: Double): Intent()
  }
}