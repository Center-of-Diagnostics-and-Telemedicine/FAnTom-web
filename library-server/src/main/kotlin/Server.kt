import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import lib.MarkTomogrammObject
import model.libraryServerPort
import repository.ResearchRepositoryImpl
import usecase.getSlice
import usecase.hounsfield
import usecase.initResearch

@ExperimentalStdlibApi
fun main() {

  embeddedServer(Netty, libraryServerPort) {

    // Serialize json
    install(ContentNegotiation) {
      register(ContentType("[*", "*]"), GsonConverter())
      register(ContentType("*", "*"), GsonConverter())
      register(ContentType("text", "plain"), GsonConverter())
      gson {}
    }
    install(DefaultHeaders)
    install(CallLogging)
    install(ConditionalHeaders)
    install(Locations)

    val researchRepository = ResearchRepositoryImpl(MarkTomogrammObject)

    routing {
      get("/hello") {
        call.respond("hello world!")
      }
      initResearch(researchRepository)
      getSlice(researchRepository)
      hounsfield(researchRepository)
    }

  }.start(wait = true)

}
