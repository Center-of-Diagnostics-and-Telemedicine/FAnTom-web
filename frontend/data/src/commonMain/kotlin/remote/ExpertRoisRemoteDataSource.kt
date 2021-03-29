package remote

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import model.*
import repository.ExpertRoisRemote

object ExpertRoisRemoteDataSource : ExpertRoisRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 600000
    }
  }

  override suspend fun getRois(token: String, researchId: Int): ExpertRoisResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$EXPERT_ROI_ROUTE")
    }
  }

  override suspend fun save(
    request: ExpertRoiEntity,
    researchId: Int,
    token: String
  ): ExpertRoisResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$EXPERT_ROI_ROUTE")
      body = Json.stringify(ExpertRoiEntity.serializer(), request)
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