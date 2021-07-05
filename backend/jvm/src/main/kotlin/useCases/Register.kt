package useCases

import hash
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import model.*
import repository.UserRepository
import util.Register
import util.user
import util.userNameValid

fun Route.register(repository: UserRepository) {

  post<Register> {
    suspend fun respondError(errorCode: ErrorStringCode) {
      call.respond(ErrorModel(errorCode.value))
    }

    //регистрировать имеет право только админ
    val user = call.user
    if (user.role != UserRole.ADMIN.value) {
      call.respond(HttpStatusCode.Forbidden)
    }

    val params = call.receive<RegistrationRequest>()
    when {
      params.name.length < 3 -> respondError(ErrorStringCode.INVALID_AUTH_CREDENTIALS)
      params.password.length < 4 -> respondError(ErrorStringCode.INVALID_REGISTER_LOGIN)
      params.role < 0 || params.role > 3 -> respondError(ErrorStringCode.INVALID_REGISTER_ROLE)
      !userNameValid(params.name) -> respondError(ErrorStringCode.INVALID_REGISTER_LOGIN)
      repository.getUser(
        params.name,
        hash(params.password)
      ) != null -> respondError(ErrorStringCode.USER_EXIST_ERROR)
      else -> {
        try {
          repository.createUser(params.name, hash(params.password), params.role)
        } catch (e: Exception) {
          application.log.error("Failed to register user", e)
          respondError(ErrorStringCode.REGISTER_FAILED)
        }
      }
    }

    call.respond(HttpStatusCode.Created)
  }

}