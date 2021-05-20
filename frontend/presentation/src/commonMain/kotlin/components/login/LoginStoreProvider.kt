package components.login

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.observeOn
import com.badoo.reaktive.coroutinesinterop.completableFromCoroutine
import com.badoo.reaktive.scheduler.mainScheduler
import model.BASE_ERROR
import model.ResearchApiExceptions
import repository.LoginRepository
import store.login.LoginStore
import store.login.LoginStore.*

internal class LoginStoreProvider(
  private val storeFactory: StoreFactory,
  private val repository: LoginRepository
) {

  fun provide(): LoginStore =
    object : LoginStore, Store<Intent, State, Label> by storeFactory.create(
      name = "LoginStore",
      initialState = State(),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {}

  private sealed class Result : JvmSerializable {
    data class LoginChanged(val login: String) : Result()
    data class PasswordChanged(val password: String) : Result()

    object Loading : Result()
    object Authorized : Result()

    data class Error(val error: String) : Result()
    object DismissErrorRequested : Result()
  }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Nothing, State, Result, Label>() {

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleLoginChanged -> handleLoginChanged(intent.text)
        is Intent.HandlePasswordChanged -> handlePasswordChanged(intent.text)
        is Intent.Auth -> login(getState)
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
      }.let {}
    }

    private fun login(getState: () -> State) {
      val state = getState()
      if (state.loading.not()) {
        dispatch(Result.Loading)
        completableFromCoroutine {
          repository.auth(state.login, state.password)
        }
          .observeOn(mainScheduler)
          .subscribeScoped(
            onComplete = ::authorized,
            onError = ::handleError
          )
      }
    }

    private fun authorized() {
      publish(Label.Authorized)
      dispatch(Result.Authorized)
    }

    private fun handleLoginChanged(login: String) {
      dispatch(Result.LoginChanged(login))
    }

    private fun handlePasswordChanged(password: String) {
      dispatch(Result.PasswordChanged(password))
    }

    private fun handleError(error: Throwable) {
      val result = when (error) {
        is ResearchApiExceptions -> Result.Error(error.error)
        else -> {
          println("login: other exception ${error.message}")
          Result.Error(BASE_ERROR)
        }
      }
      dispatch(result)
    }

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