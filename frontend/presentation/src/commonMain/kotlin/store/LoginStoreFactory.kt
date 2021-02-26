package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.subscribeOn
import model.BASE_ERROR
import model.ResearchApiExceptions
import model.ResearchApiExceptions.AuthFailedException
import repository.LoginRepository
import store.login.LoginStore.*
import store.login.LoginStoreAbstractFactory

internal class LoginStoreFactory(
  storeFactory: StoreFactory,
  val repository: LoginRepository
) : LoginStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Nothing, State, Result, Label> = ExecutorImpl()

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
        singleFromCoroutine {
          repository.auth(state.login, state.password)
        }
          .subscribeOn(ioScheduler)
          .observeOn(mainScheduler)
          .subscribeScoped(
            isThreadLocal = true,
            onSuccess = {
              publish(Label.Authorized)
              dispatch(Result.Authorized)
            },
            onError = ::handleError
          )
      }
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
}
