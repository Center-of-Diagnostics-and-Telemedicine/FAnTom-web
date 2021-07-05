package util

import UserModel
import io.ktor.application.*
import io.ktor.auth.*

private val userIdPattern = "[a-zA-Z0-9_\\.]+".toRegex()

internal fun userNameValid(userName: String) = userName.matches(userIdPattern)

val ApplicationCall.user get() = authentication.principal<UserModel>()!!
