
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ConditionalHeaders
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.locations.Locations
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import lib.MarkTomogrammObject
import repository.ResearchRepositoryImpl
import usecase.getSlice
import usecase.hounsfield
import usecase.initResearch

@ExperimentalStdlibApi
fun main() {

  embeddedServer(Netty, 8082) {

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


