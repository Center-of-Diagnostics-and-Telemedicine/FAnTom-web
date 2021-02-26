package remote

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import model.AUTH_CHECK_ROUTE
import model.AuthorizationRequest
import model.AuthorizationResponse
import model.LOGIN_ROUTE
import repository.LoginRemote

object LoginRemoteDataSource : LoginRemote {

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
      takeFrom(model.MAIN_SERVER_URL)
      encodedPath = path
    }
  }

}