import controller.LoginController
import controller.ResearchController
import fantom.SessionManager
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.routing.*
import model.*
import util.*

fun Application.main(
  loginController: LoginController,
  researchController: ResearchController
) {

  processRequests(
    loginController,
    researchController
  )
}

fun Application.processRequests(
  loginController: LoginController,
  researchController: ResearchController
) {
  routing {

    static {
      resource(ROOT, RESOURCE_INDEX)

      static(STATIC_ROUTE) {
        resources(RESOURCE_STATIC)
      }
    }

    post("/login") {
      loginController.login(call)
    }

    authenticate("jwt") {

      post("register") {
        loginController.register(call)
      }

      route(SESSION_ROUTE) {
        get(CLOSE_ROUTE) {
          SessionManager.closeSession(call.user)
          call.respond(HttpStatusCode.OK)
        }
      }

      route(RESEARCH_ROUTE) {
        get(LIST_ROUTE) {
          researchController.list(call)
        }
        get("$INIT_ROUTE/{id}") {
          researchController.init(call)
        }
        post("{id}") {
          researchController.getSlice(call)
        }
        get(HOUNSFIELD_ROUTE) {
          researchController.hounsfield(call)
        }
        get("$CLOSE_ROUTE/{id}") {
          researchController.close(call)
        }
      }
    }
  }
}
