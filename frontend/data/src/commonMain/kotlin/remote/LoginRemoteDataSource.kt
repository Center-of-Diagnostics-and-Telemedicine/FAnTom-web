package remote

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import model.AUTH_CHECK_ROUTE
import model.AuthorizationRequest
import model.AuthorizationResponse
import model.LOGIN_ROUTE

interface LoginRemote {
    suspend fun auth(request: AuthorizationRequest): AuthorizationResponse
    suspend fun tryToAuth(): AuthorizationResponse
}

class LoginRemoteDataSource: LoginRemote{

    private val client: HttpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override suspend fun auth(request: AuthorizationRequest): AuthorizationResponse {
        return client.post {
            apiUrl(LOGIN_ROUTE)
            body = Json.stringify(AuthorizationRequest.serializer(), request)
        }
    }

    override suspend fun tryToAuth(): AuthorizationResponse {
        return client.post {
            apiUrl(AUTH_CHECK_ROUTE)
        }
    }

    private fun HttpRequestBuilder.apiUrl(path: String) {
        url {
            takeFrom(model.END_POINT)
            encodedPath = path
        }
    }

}