package model

import kotlinx.serialization.Serializable


@Serializable
data class AuthorizationRequest(val name: String, val password: String)

@Serializable
data class RegistrationRequest(val name: String, val password: String, val role: Int)
