package remote

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import model.*
import repository.ExpertMarksRemote

object ExpertMarksRemoteDataSource : ExpertMarksRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 600000
    }
  }

  override suspend fun getMark(token: String, researchId: Int): ExpertMarksResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$EXPERT_MARK_ROUTE")
    }
  }

  override suspend fun save(
    request: ExpertMarkEntity,
    researchId: Int,
    token: String
  ): ExpertMarksResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$EXPERT_MARK_ROUTE")
      body = Json.encodeToString(ExpertMarkEntity.serializer(), request)
    }
  }

  override suspend fun update(
    request: ExpertMarkEntity,
    researchId: Int,
    token: String
  ): BaseResponse {
    return client.patch {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$EXPERT_MARK_ROUTE")
      body = Json.encodeToString(ExpertMarkEntity.serializer(), request)
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