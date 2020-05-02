package client.newmvi.login.store

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable

interface LoginStore : Disposable {

  val states: Observable<State>

  fun accept(intent: Intent)

  data class State(
    val isLoading: Boolean = false,
    val error: String = "",
    val authorized: Boolean = false
  )

  sealed class Intent {
    object Init : Intent()
    class Auth(val login: String, val password: String) : Intent()

    object DismissError : Intent()
  }
}


