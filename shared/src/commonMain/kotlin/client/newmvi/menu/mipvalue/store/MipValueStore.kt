package client.newmvi.menu.mipvalue.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import model.INITIAL_MIP_VALUE

interface MipValueStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val value: Int = INITIAL_MIP_VALUE,
    val available: Boolean = false
  )

  sealed class Intent {
    object EnableMip: Intent()
    object DisableMip: Intent()

    data class ChangeValue(val value: Int) : Intent()
    data class NewValueFromSubscription(val value: Int) : Intent()
  }
}