import controller.*
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level

fun main() {

  embeddedServer(Netty, 8081) {

    // Serialize json
    install(ContentNegotiation) {
      register(ContentType("[*", "*]"), GsonConverter())
      register(ContentType("*", "*"), GsonConverter())
      register(ContentType("text", "plain"), GsonConverter())
      gson {}
    }

    // Return custom errors (if needed)
//    install(StatusPages) {
//      exception<AuthenticationException> {
//        call.respond(HttpStatusCode.Unauthorized)
//      }
//    }
    install(CORS) {
      anyHost()
      allowCredentials = true
      allowNonSimpleContentTypes = true
      allowSameOrigin = true
      header(HttpHeaders.Authorization)
      header(HttpHeaders.Expires)
      header(HttpHeaders.LastModified)
      header(HttpHeaders.AccessControlAllowHeaders)
      header(HttpHeaders.ContentType)
      header(HttpHeaders.AccessControlAllowOrigin)
      header(HttpHeaders.AcceptEncoding)
      header(HttpHeaders.AcceptLanguage)
      header(HttpHeaders.AccessControlRequestHeaders)
      header(HttpHeaders.AccessControlRequestMethod)
      header(HttpHeaders.UserAgent)


      method(HttpMethod.Options)
      method(HttpMethod.Get)
      method(HttpMethod.Post)
      method(HttpMethod.Put)
      method(HttpMethod.Delete)
      method(HttpMethod.Patch)
    }
    install(DefaultHeaders)
    install(ConditionalHeaders)

    install(CallLogging) {
      level = Level.DEBUG
    }

    // Modules
    main(ResearchControllerImpl())
  }.start(wait = true)

}


