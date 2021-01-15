package remote

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import model.*
import repository.MarksRemote

object MarksRemoteDataSource : MarksRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 600000
    }
  }

  override suspend fun getAll(token: String, researchId: Int): MarksResponseNew {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$MARK_ROUTE/$LIST_ROUTE")
    }
  }

  override suspend fun save(request: MarkData, researchId: Int, token: String): MarkResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$MARK_ROUTE")
      body = Json.stringify(MarkData.serializer(), request)
    }
  }

  override suspend fun update(request: MarkEntity, researchId: Int, token: String): BaseResponse {
    return client.put {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$MARK_ROUTE")
      body = Json.stringify(MarkEntity.serializer(), request)
    }
  }

  override suspend fun delete(markId: Int, researchId: Int, token: String): BaseResponse {
    return client.delete {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$MARK_ROUTE/$markId")
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
