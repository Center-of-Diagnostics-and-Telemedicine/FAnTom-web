package controller

import JwtConfig
import createUser
import getUserByCredentials
import io.ktor.application.ApplicationCall
import io.ktor.auth.UserPasswordCredential
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.request.receiveOrNull
import io.ktor.response.respond
import model.*
import util.debugLog
import util.user

class LoginController {


  suspend fun login(call: ApplicationCall) {

    val credentials = call.receive<UserPasswordCredential>()

    try {
      val user = getUserByCredentials(credentials)
      if (user == null) {
        debugLog("user not found")
        call.respond(HttpStatusCode.Forbidden)
      } else {
        debugLog("user found")
        val token = JwtConfig.makeToken(userModel = user)
//        populateResearches(user.id)
        call.respond(AuthorizationResponse(token = token))
      }
    } catch (e: Throwable) {
      println(e.toString())
      call.respond(HttpStatusCode.Forbidden)
    }

  }

  suspend fun register(call: ApplicationCall) {

    if (call.user.role != UserRole.ADMIN.value) {
      call.respond(HttpStatusCode.Forbidden, "ты не админ")
    }

    try {
      val request = call.receiveOrNull<RegistrationRequest>()
      if (request != null) {
        val exists = getUserByCredentials(
          UserPasswordCredential(request.name, hash(request.password))
        ) != null
        if (exists) {
          call.respond(HttpStatusCode.Conflict, message = "пользователь с таким именем существует")
        } else {
          createUser(request.name, hash(request.password), request.role)
//          populateResearches(user)
          call.respond(HttpStatusCode.Created)
        }
      }
    } catch (e: Throwable) {
      println(e.toString())
      call.respond(HttpStatusCode.Forbidden, "ошибка у нас тут...")
    }

  }

//  private suspend fun populateResearches(id: Int) {
//    debugLog("populateResearches")
//    val accessionNames = SessionManager.getInstanceForUser(id).getAccessionNames()
//    val researches = getResearches(id)
//    val researchesToUpdate: MutableList<String> = mutableListOf()
//    accessionNames.forEach { accessionName ->
//      val research = researches.firstOrNull { it.name == accessionName }
//      if (research == null) {
//        researchesToUpdate.add(accessionName)
//      }
//    }
//    if (researchesToUpdate.isNotEmpty()) {
//      updateResearches(researchesToUpdate)
//    }
//  }
}