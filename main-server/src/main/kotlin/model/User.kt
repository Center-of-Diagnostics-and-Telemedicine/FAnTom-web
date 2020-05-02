package model

import io.ktor.auth.Principal
import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val hashKey = hex("4c6f6e67204c69766520416672696b6121")
val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hash(password: String): String {
  val hmac = Mac.getInstance("HmacSHA1")
  hmac.init(hmacKey)
  return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

data class User(
  val id: Int,
  val name: String,
  val password: String,
  val role: Int
) : Principal