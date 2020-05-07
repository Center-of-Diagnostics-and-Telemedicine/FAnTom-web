import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import lib.MarkTomogrammObject
import repository.ResearchRepositoryImpl
import usecase.*

fun main() {

  embeddedServer(Netty, 8081) {

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
      initResearch(researchRepository)
      getSlice(researchRepository)
      hounsfield(researchRepository)
    }

    // Modules
//    main(ResearchControllerImpl())
  }.start(wait = true)

}


