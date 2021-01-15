package client.newmvi.login.store

import client.ResearchApiExceptions
import client.debugLog
import client.domain.repository.LoginRepository
import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.*
import model.AUTH_FAILED

interface LoginProcessor {

  @EventsOnAnyScheduler
  fun auth(login: String, password: String): Single<Result>

  @EventsOnAnyScheduler
  fun tryToAuth(): Single<Result>

  sealed class Result {
    object Success : Result()
    data class Error(val message: String) : Result()
    object InvalidCredentials : Result()
  }
}

class LoginProcessorImpl(
  private val repository: LoginRepository
) : LoginProcessor {

  override fun auth(login: String, password: String): Single<LoginProcessor.Result> =
    singleFromCoroutine {
      repository.auth(login, password)
    }
      .map { LoginProcessor.Result.Success }
      .onErrorResumeNext {
        when (it) {
          is ResearchApiExceptions.AuthFailedException -> {
            debugLog("AuthFailedException, ${it.error}")
            LoginProcessor.Result.Error(it.error).toSingle()
          }
          is ResearchApiExceptions.InvalidAuthCredentials -> {
            debugLog("InvalidCredentials, ${it.error}")
            LoginProcessor.Result.InvalidCredentials.toSingle()
          }
          else -> {
            debugLog("other exception ${it.message}")
            LoginProcessor.Result.Error(AUTH_FAILED).toSingle()
          }
        }
      }

  override fun tryToAuth(): Single<LoginProcessor.Result> =
    singleFromCoroutine {
      repository.tryToAuth()
    }
      .map { LoginProcessor.Result.Success }
      .onErrorReturnValue(LoginProcessor.Result.Error("Произошла ошибка"))

}