
import io.ktor.auth.*
import io.ktor.util.*
import model.UserModel
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

val hashKey = hex("4c6f6e67204c69766520416672696b6121")
val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

fun hash(password: String): String {
  val hmac = Mac.getInstance("HmacSHA1")
  hmac.init(hmacKey)
  return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

class UserModel(
  override val id: Int,
  override val name: String,
  override val password: String,
  override val role: Int
) : UserModel, Principal