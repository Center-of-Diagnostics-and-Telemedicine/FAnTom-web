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
import model.MAIN_SERVER_PORT
import org.apache.http.auth.AuthenticationException
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level
import useCases.*
import util.Config
import util.parseConfig

lateinit var config: Config

/**
Copyright (c) 2021, Moscow Center for Diagnostics & Telemedicine
All rights reserved.
This file is licensed under BSD-3-Clause license. See LICENSE file for details.
 */


fun main(args: Array<String>) {

  config = parseConfig(args)

  val jwt = "jwt"

  embeddedServer(Netty, MAIN_SERVER_PORT) {

    Database.connect(
      url = "jdbc:mysql://localhost:3306/fantom_mg?characterEncoding=utf8&useUnicode=true&useSSL=false",
      driver = "com.mysql.jdbc.Driver",
      user = "root",
//      password = "vfrcbv16"
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
      level = Level.DEBUG
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
//        resource(ROOT, RESOURCE_JS)

        static(STATIC_ROUTE) {
          resources(RESOURCE_STATIC)
        }
      }

      login(userRepository)

      authenticate(jwt) {
        register(userRepository)

        researchesList(researchRepository, userResearchRepository, covidMarksRepository)
        initResearch(researchRepository, sessionRepository, userResearchRepository)
        closeResearch(sessionRepository, researchRepository, userResearchRepository)

        getSlice(researchRepository, sessionRepository)
        hounsfield(sessionRepository)
        closeSession(sessionRepository)

        mark(covidMarksRepository)
        getCovidMark(covidMarksRepository, researchRepository)

        getMarks(multiPlanarMarksRepository, planarMarksRepository, researchRepository)
        createMark(multiPlanarMarksRepository, planarMarksRepository, researchRepository)
        deleteMark(multiPlanarMarksRepository, planarMarksRepository, researchRepository)
        updateMark(multiPlanarMarksRepository, planarMarksRepository, researchRepository)
      }

    }
  }.start(wait = true)

}
