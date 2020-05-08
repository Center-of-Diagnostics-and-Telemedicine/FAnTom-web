package client.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.http.takeFrom
import model.*

interface LoginRemote {
  suspend fun auth(login: String, password: String): AuthorizationResponse
  suspend fun tryToAuth(): AuthorizationResponse
}

class LoginRemoteDataSource : LoginRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
  }

  override suspend fun auth(login: String, password: String): AuthorizationResponse {
    return client.post {
      apiUrl(LOGIN_ROUTE)
      body = "{ name:$login, password:$password }"
    }
  }

  override suspend fun tryToAuth(): AuthorizationResponse {
    return client.post {
      apiUrl(AUTH_CHECK_ROUTE)
    }
  }

  private fun HttpRequestBuilder.apiUrl(path: String) {
    url {
      takeFrom(END_POINT)
      encodedPath = path
    }
  }

}