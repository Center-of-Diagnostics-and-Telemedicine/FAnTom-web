package com.marktomogram.backend

import controller.ResearchControllerImpl
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import lib.MarkTomogrammObject
import org.slf4j.event.Level

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) {
        args[0].toInt()
    } else {
        8081
    }
    embeddedServer(Netty, port) {

        // Serialize json
        install(ContentNegotiation) {
            register(ContentType("[*", "*]"), GsonConverter())
            register(ContentType("*", "*"), GsonConverter())
            register(ContentType("text", "plain"), GsonConverter())
            gson {}
        }

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

        MarkTomogrammObject.init()

        // Modules
        main(ResearchControllerImpl())
    }.start(wait = true)

}


