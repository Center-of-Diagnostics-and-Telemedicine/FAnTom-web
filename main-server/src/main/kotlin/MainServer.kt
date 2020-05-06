import controller.LoginController
import controller.ResearchController
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.apache.http.auth.AuthenticationException
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level


fun main() {
  embeddedServer(Netty, 80) {

    // Database
    Database.connect(
      url = "jdbc:mysql://localhost:3306/mark_tomogram?characterEncoding=utf8&useUnicode=true",
      driver = "com.mysql.jdbc.Driver",
      user = "root",
      password = "vfrcbv16"
    )

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
    install(ConditionalHeaders)
    install(CallLogging)
    install(ConditionalHeaders)
    install(Authentication) {
      jwt("jwt") {
        verifier(JwtConfig.verifier)
        realm = "ktor.io"
        validate {
          it.payload.getClaim("id").asInt()?.let(::getUser)
        }
      }
    }

    //create tables if not exists
    init()

//    routing{
//      login()
//      researchList()
//      research()
//    }

//    DBMigration.migrate()

    // Modules
    main(LoginController(), ResearchController())
  }.start(wait = true)

}




