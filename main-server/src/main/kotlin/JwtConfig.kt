import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import model.User
import util.AUTHENTICATION
import util.ID_FIELD
import util.NAME_FIELD
import util.PASSWORD_FIELD
import java.util.*

object JwtConfig {

    private const val secret = "zAP5MBA4B4Ijz0MZaS48"
    private const val issuer = "ktor.io"
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(user: User): String {
        return JWT.create()
                .withSubject(AUTHENTICATION)
                .withIssuer(issuer)
                .withClaim(ID_FIELD, user.id)
                .withClaim(NAME_FIELD, user.name)
                .withClaim(PASSWORD_FIELD, user.password)
                .withExpiresAt(getExpiration())
                .sign(algorithm)
    }

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

}