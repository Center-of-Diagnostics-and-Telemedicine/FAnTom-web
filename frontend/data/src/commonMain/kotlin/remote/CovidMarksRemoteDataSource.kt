package remote

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import model.*
import repository.CovidMarksRemote

object CovidMarksRemoteDataSource : CovidMarksRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 600000
    }
  }

  override suspend fun getMark(token: String, researchId: Int): CovidMarksResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$COVID_MARK_ROUTE")
    }
  }

  override suspend fun save(
    request: CovidMarkEntity,
    researchId: Int,
    token: String
  ): CovidMarksResponse {
    return client.put {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$COVID_MARK_ROUTE")
      body = Json.stringify(CovidMarkEntity.serializer(), request)
    }
  }

  private fun HttpRequestBuilder.authHeader(token: String) {
    header("Authorization", "Bearer $token")
  }

  private fun HttpRequestBuilder.apiUrl(path: String) {
    url {
      takeFrom(MAIN_SERVER_URL)
      encodedPath = path
    }
  }
}