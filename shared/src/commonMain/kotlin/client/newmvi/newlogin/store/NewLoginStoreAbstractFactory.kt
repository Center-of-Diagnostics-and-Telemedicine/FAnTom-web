package client.newmvi.newlogin.store

import client.newmvi.newlogin.store.NewLoginStore.Intent
import client.newmvi.newlogin.store.NewLoginStore.State
import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen

abstract class NewLoginStoreAbstractFactory(
  private val storeFactory: StoreFactory
) {

  fun create(): NewLoginStore =
    object : NewLoginStore, Store<Intent, State, Nothing> by storeFactory.create(
      name = "NewLoginStore",
      initialState = State(),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected sealed class Result : JvmSerializable {
    data class LoginChanged(val login: String) : Result()
    data class PasswordChanged(val password: String) : Result()

    object Loading : Result()
    object Authorized : Result()

    class Error(val error: String) : Result()
    object DismissErrorRequested : Result()
  }

  protected abstract fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing>

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.LoginChanged -> copy(login = login)
        is Result.PasswordChanged -> copy(password = password)
        Result.Loading -> copy(loading = true)
        Result.Authorized -> copy(authorized = authorized, loading = false)
        is Result.Error -> copy(error = error, loading = false)
        Result.DismissErrorRequested -> copy(error = "")
      }
  }
}
