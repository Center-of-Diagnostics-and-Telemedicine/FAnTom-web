package client.newmvi.menu.gamma.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.INITIAL_GAMMA

interface GammaStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val value: Double = INITIAL_GAMMA
  )

  sealed class Intent {
    data class Drag(val value: Double): Intent()
    data class NewValueFromSubscription(val value: Double): Intent()
  }
}