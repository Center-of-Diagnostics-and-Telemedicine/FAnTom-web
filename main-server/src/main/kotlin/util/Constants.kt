package util

import model.User
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication

const val AUTHENTICATION = "Authentication"
const val ID_FIELD = "id"
const val NAME_FIELD = "name"
const val PASSWORD_FIELD = "password"
const val ROLE_FIELD = "role"
const val SEEN_FIELD = "seen"
const val DONE_FIELD = "done"
const val MARKED_FIELD = "marked"
const val USER_ID_FIELD = "user_id"
const val RESEARCH_ID_FIELD = "research_id"

const val USER_TABLE = "user"
const val RESEARCH_TABLE = "research"
const val MARKS_TABLE = "marks"

const val ROOT = "/"
const val STATIC_ROUTE = "/static"
const val RESOURCE_STATIC = "static/static"
const val RESOURCE_INDEX = "static/index.html"

const val basePort = 8080
const val fantomServerPort = 8081

val ApplicationCall.user get() = authentication.principal<User>()!!