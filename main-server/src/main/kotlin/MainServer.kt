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
import io.ktor.server.netty.*
import model.*
import org.apache.http.auth.AuthenticationException
import org.jetbrains.exposed.sql.Database
import org.slf4j.event.Level
import useCases.*

/**
Copyright (c) 2021, Moscow Center for Diagnostics & Telemedicine
All rights reserved.
This file is licensed under BSD-3-Clause license. See LICENSE file for details.
 */

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

  val jwt = "jwt"

  Database.connect(
    url = DATABASE_URL,
    driver = DATABASE_DRIVER,
    user = DATABASE_USER,
    password = DATABASE_PASSWORD
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

}
