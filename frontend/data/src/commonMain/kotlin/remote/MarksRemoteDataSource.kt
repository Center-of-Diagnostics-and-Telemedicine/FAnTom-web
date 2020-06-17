package remote

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.takeFrom
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

  override suspend fun update(request: MarkDomain, token: String): BaseResponse {
    return client.put {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$MARK_ROUTE")
      body = Json.stringify(MarkDomain.serializer(), request)
    }
  }

  override suspend fun delete(markId: Int, token: String): BaseResponse {
    return client.delete {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$MARK_ROUTE/$markId")
    }
  }

  private fun HttpRequestBuilder.authHeader(token: String) {
    header("Authorization", "Bearer $token")
  }

  private fun HttpRequestBuilder.apiUrl(path: String) {
    url {
      takeFrom(LOCALHOST)
      encodedPath = path
    }
  }
}
