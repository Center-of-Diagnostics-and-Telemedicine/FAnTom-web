package store.login

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import store.login.LoginStore.*

abstract class LoginStoreAbstractFactory(
    private val storeFactory: StoreFactory
) {

  fun create(): LoginStore =
    object : LoginStore, Store<Intent, State, Label> by storeFactory.create(
      name = "LoginStore",
      initialState = State(),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    data class LoginChanged(val login: String) : Result()
    data class PasswordChanged(val password: String) : Result()

    object Loading : Result()
    object Authorized : Result()

    data class Error(val error: String) : Result()
    object DismissErrorRequested : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
          is Result.LoginChanged -> copy(login = result.login)
          is Result.PasswordChanged -> copy(password = result.password)
          is Result.Loading -> copy(loading = true)
          is Result.Authorized -> copy(loading = false)
          is Result.Error -> copy(error = result.error, loading = false)
          is Result.DismissErrorRequested -> copy(error = "")
      }
  }
}
