package client.newmvi.menu.black.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.INITIAL_BLACK

interface BlackStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val value: Double = INITIAL_BLACK
  )

  sealed class Intent {
    data class ChangeValue(val value: Double) : Intent()
    data class NewValueFromSubscription(val value: Double) : Intent()
  }
}