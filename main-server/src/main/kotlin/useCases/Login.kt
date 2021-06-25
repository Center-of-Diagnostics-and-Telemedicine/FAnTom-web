package useCases

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.post
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.Route
import model.*
import repository.UserRepository
import util.Login
import util.userNameValid

fun Route.login(repository: UserRepository, makeToken: (userModel: UserModel) -> String) {

  post<Login> {
    val credentials = call.receive<UserPasswordCredential>()
    val user = when {
      credentials.name.length < 3 -> null
      credentials.password.length < 4 -> null
      !userNameValid(credentials.name) -> null
      else -> repository.getUser(credentials.name, hash(credentials.password))
    }

    if (user == null) {

      call.respond(
        AuthorizationResponse(
          error = ErrorModel(ErrorStringCode.INVALID_AUTH_CREDENTIALS.value)
        )
      )
    } else {
      val token = makeToken(user)
      call.respond(
        AuthorizationResponse(
          response = AuthorizationModel(token = token)
        )
      )
    }
  }
}