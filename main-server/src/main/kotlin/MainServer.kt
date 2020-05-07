import controller.LoginController
import controller.ResearchController
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.apache.http.auth.AuthenticationException
import useCases.*


fun main() {
  val jwt = "jwt"

  embeddedServer(Netty, 80) {

    // Serialize json
    install(ContentNegotiation) {
      register(ContentType("[*", "*]"), GsonConverter())
      register(ContentType("*", "*"), GsonConverter())
      register(ContentType("text", "plain"), GsonConverter())
      gson {}
    }
    install(StatusPages) {
      exception<AuthenticationException> {
        call.respond(HttpStatusCode.Unauthorized)
      }
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(ConditionalHeaders)
    install(Authentication) {
      jwt(jwt) {
        verifier(JwtConfig.verifier)
        realm = "ktor.io"
        validate {
          it.payload.getClaim("id").asInt()?.let(::getUser)
        }
      }
    }

    //create tables if not exists
    init()

    routing {
      login(userRepository)

      authenticate(jwt) {
        register(userRepository)
        researchesList(researchRepository, userResearchRepository, marksRepository)
        initResearch(researchRepository, sessionRepository)
        getSlice(researchRepository, sessionRepository)
        hounsfield(sessionRepository)
        mark(marksRepository)
        closeSession(researchRepository, sessionRepository)
      }

    }

//    DBMigration.migrate()
  }.start(wait = true)

}




