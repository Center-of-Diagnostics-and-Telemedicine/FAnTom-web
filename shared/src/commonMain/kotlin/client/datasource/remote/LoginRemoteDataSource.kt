package client.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.http.takeFrom
import model.AuthorizationResponse
import model.*

class LoginRemoteDataSource : LoginRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
  }

  override suspend fun auth(login: String, password: String): String {
    return client.post<AuthorizationResponse> {
      apiUrl(LOGIN_ROUTE)
      body = "{ name:$login, password:$password }"
    }.token
  }

  override suspend fun tryToAuth(): String {
    return client.post<AuthorizationResponse> {
      apiUrl(AUTH_CHECK_ROUTE)
    }.token
  }

  private fun HttpRequestBuilder.apiUrl(path: String) {
    url {
      takeFrom(END_POINT)
      encodedPath = path
    }
  }

}