package client.newmvi.login.store

import com.badoo.reaktive.annotations.EventsOnAnyScheduler
import com.badoo.reaktive.coroutinesinterop.singleFromCoroutine
import com.badoo.reaktive.single.Single
import com.badoo.reaktive.single.map
import com.badoo.reaktive.single.onErrorReturnValue
import client.domain.repository.LoginRepository

interface LoginProcessor {
  @EventsOnAnyScheduler
  fun auth(login: String, password: String): Single<Result>

  @EventsOnAnyScheduler
  fun tryToAuth(): Single<Result>

  sealed class Result {
    object Success : Result()
    data class Error(val message: String) : Result()
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
      .onErrorReturnValue(LoginProcessor.Result.Error("Произошла ошибка при авторизации"))

  override fun tryToAuth(): Single<LoginProcessor.Result> =
    singleFromCoroutine {
      repository.tryToAuth()
    }
      .map { LoginProcessor.Result.Success }
      .onErrorReturnValue(LoginProcessor.Result.Error("Произошла ошибка"))

}