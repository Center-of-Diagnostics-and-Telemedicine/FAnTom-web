import controller.LoginController
import controller.ResearchController
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.html.respondHtml
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.css.*
import kotlinx.css.properties.lh
import kotlinx.html.*
import model.*

fun Application.main(
    loginController: LoginController,
    researchController: ResearchController
) {

    processRequests(
        loginController,
        researchController
    )
}

fun Application.processRequests(
    loginController: LoginController,
    researchController: ResearchController
) {
    routing {

        get("/") {
            val globalCss = CSSBuilder().apply {
                body {
                    margin(0.px)
                    padding(0.px)

                    fontSize = 13.px
                    fontFamily = "-system-ui, -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, BlinkMacSystemFont, Segoe UI, Roboto, Oxygen, Ubuntu, Cantarell, Droid Sans, Helvetica Neue, Arial, sans-serif"

                    lineHeight = 20.px.lh
                }
            }

            call.respondHtml {
                head {
                    meta {
                        charset = "utf-8"
                    }
                    title {
                        +"Kotlin full stack application demo"
                    }
                    style {
                        unsafe {
                            +globalCss.toString()
                        }
                    }
                }
                body {
                    div {
                        id = "react-app"
                        +"Loading..."
                    }
                    script(src = "/client.js") {
                    }
                }
            }
        }

        static("/") {
            resources("/")
        }

        post("/login") {
            loginController.login(call)
        }

        get("hello") {
            call.respond("Hello world")
        }

        authenticate("jwt") {

            post("register") {
                loginController.register(call)
            }

            route(RESEARCH_ROUTE) {
                get(LIST_ROUTE) {
                    researchController.list(call)
                }
                get("$INIT_ROUTE/{id}") {
                    researchController.init(call)
                }
                post("{id}") {
                    researchController.getSlice(call)
                }
                get(HOUNSFIELD_ROUTE) {
                    researchController.hounsfield(call)
                }
                get("$CLOSE_RESEARCH_ROUTE/{id}") {
                    researchController.close(call)
                }
            }
        }
    }
}
