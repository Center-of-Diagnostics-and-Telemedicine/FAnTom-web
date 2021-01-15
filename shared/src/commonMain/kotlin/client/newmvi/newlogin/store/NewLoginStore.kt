package client.newmvi.newlogin.store

import client.newmvi.newlogin.store.NewLoginStore.Intent
import client.newmvi.newlogin.store.NewLoginStore.State
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.JvmSerializable


interface NewLoginStore : Store<Intent, State, Nothing> {

  sealed class Intent : JvmSerializable {
    data class HandleLoginChanged(val login: String) : Intent()
    data class HandlePasswordChanged(val password: String) : Intent()
    data class Login(val login: String, val password: String) : Intent()
    object DismissError : Intent()
  }

  data class State(
    val login: String = "",
    val password: String = "",
    val authorized: Boolean = false,
    val loading: Boolean = false,
    val error: String = ""
  ) : JvmSerializable
}