package client.newmvi.newlogin.store

import client.ResearchApiExceptions
import client.debugLog
import client.domain.repository.LoginRepository
import client.newmvi.newlogin.store.NewLoginStore.Intent
import client.newmvi.newlogin.store.NewLoginStore.State
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.*
import model.AUTH_FAILED

internal class NewLoginStoreFactory(
  storeFactory: StoreFactory,
  private val repository: LoginRepository
) : NewLoginStoreAbstractFactory(
  storeFactory = storeFactory
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
//        override fun executeAction(action: Unit, getState: () -> State) {
//            //todo(тут можно воткнуть tryToAuth(), не забыть добавть bootstraper)
//        }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleLoginChanged -> dispatch(Result.LoginChanged(intent.login))
        is Intent.HandlePasswordChanged -> dispatch(Result.PasswordChanged(intent.password))
        is Intent.Login -> login(intent.login, intent.password)
        Intent.DismissError -> dispatch(Result.DismissErrorRequested)
      }.let {}
    }

    private fun login(login: String, password: String) {
      dispatch(Result.Loading)
      singleFromCoroutine {
        repository.auth(login, password)
      }
        .map { Result.Authorized }
        .onErrorResumeNext {
          when (it) {
            is ResearchApiExceptions.AuthFailedException -> Result.Error(it.error)
            is ResearchApiExceptions.InvalidAuthCredentials -> Result.Error(it.error)
            else -> {
              debugLog("login: other exception ${it.message}")
              Result.Error(AUTH_FAILED)
            }
          }.toSingle()
        }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(isThreadLocal = true, onSuccess = ::dispatch)
    }
  }
}
