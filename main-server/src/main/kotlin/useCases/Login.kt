package useCases

import JwtConfig
import io.ktor.application.call
import io.ktor.auth.UserPasswordCredential
import io.ktor.locations.post
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import model.*
import repository.UserRepository
import util.Login
import util.userNameValid

fun Route.login(repository: UserRepository) {

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
      val token = JwtConfig.makeToken(userModel = user)
      call.respond(
        AuthorizationResponse(
          response = AuthorizationModel(token = token)
        )
      )
    }
  }
}