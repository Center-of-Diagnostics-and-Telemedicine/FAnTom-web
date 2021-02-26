package store.login

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import store.login.LoginStore.*

interface LoginStore : Store<Intent, State, Label> {

  sealed class Intent : JvmSerializable {
    data class HandleLoginChanged(val text: String) : Intent()
    data class HandlePasswordChanged(val text: String) : Intent()
    object Auth : Intent()
    object DismissError : Intent()
  }

  data class State(
      val login: String = "",
      val password: String = "",
      val loading: Boolean = false,
      val error: String = ""
  ) : JvmSerializable

  sealed class Label : JvmSerializable {
    object Authorized : Label()
  }
}
