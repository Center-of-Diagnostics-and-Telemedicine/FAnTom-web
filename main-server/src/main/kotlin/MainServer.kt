
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import model.ID_FIELD
import org.apache.http.auth.AuthenticationException
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level
import useCases.*
import util.*

lateinit var config: Config


fun main(args: Array<String>) {

  config = parseConfig(args)

  val jwt = "jwt"

  embeddedServer(Netty, 8081) {

    Database.connect(
      url = "jdbc:mysql://localhost:3306/mark_tomogram?characterEncoding=utf8&useUnicode=true&useSSL=false",
      driver = "com.mysql.jdbc.Driver",
      user = "root",
      password = ""
    )

    // Serialize json
    install(ContentNegotiation) {
      register(ContentType("[*", "*]"), GsonConverter())
      register(ContentType("*", "*"), GsonConverter())
      register(ContentType("text", "plain"), GsonConverter())
      gson {}
    }
    install(CORS) {
      method(HttpMethod.Options)
      method(HttpMethod.Get)
      method(HttpMethod.Post)
      method(HttpMethod.Put)
      method(HttpMethod.Delete)
      method(HttpMethod.Patch)
      header(HttpHeaders.AccessControlAllowHeaders)
      header(HttpHeaders.ContentType)
      header(HttpHeaders.AccessControlAllowOrigin)
      header(HttpHeaders.Authorization)
      anyHost()
      allowCredentials = true
    }
    install(StatusPages) {
      exception<AuthenticationException> {
        call.respond(HttpStatusCode.Unauthorized)
      }
    }
    install(DefaultHeaders)
    install(CallLogging) {
      level = Level.INFO
    }
    install(ConditionalHeaders)
    install(Locations)
    install(Authentication) {
      jwt(jwt) {
        verifier(JwtConfig.verifier)
        realm = "ktor.io"
        validate {
          it.payload.getClaim(ID_FIELD).asInt()?.let(::getUser)
        }
      }
    }

    //create tables if not exists
    init()

    routing {
      static {
        resource(ROOT, RESOURCE_INDEX)
        resource(ROOT, RESOURCE_JS)

        static(STATIC_ROUTE) {
          resources(RESOURCE_STATIC)
        }
      }

      login(userRepository)

      authenticate(jwt) {
        register(userRepository)
        researchesList(researchRepository, userResearchRepository, covidMarksRepository)
        initResearch(researchRepository, sessionRepository, userResearchRepository)
        getSlice(researchRepository, sessionRepository)
        hounsfield(sessionRepository)
        mark(covidMarksRepository)
        closeSession(researchRepository, sessionRepository)

        getMarks(marksRepository)
        createMark(marksRepository)
        deleteMark(marksRepository)
        updateMark(marksRepository)
      }

    }
  }.start(wait = true)

}
