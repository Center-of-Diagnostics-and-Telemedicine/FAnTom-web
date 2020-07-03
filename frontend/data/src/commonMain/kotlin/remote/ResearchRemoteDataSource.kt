package remote

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import model.*
import repository.ResearchRemote

object ResearchRemoteDataSource : ResearchRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 600000
    }
  }

  override suspend fun getAll(token: String): ResearchesResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$LIST_ROUTE")
    }
  }

  override suspend fun init(
    token: String,
    id: Int
  ): ResearchInitResponseNew {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$INIT_ROUTE/$id")
    }
  }

  override suspend fun getSlice(
    token: String,
    request: SliceRequestNew,
    researchId: Int
  ): SliceResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId")
      body = Json.stringify(SliceRequestNew.serializer(), request)
    }
  }

  override suspend fun hounsfield(
    token: String,
    request: HounsfieldRequest
  ): HounsfieldResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE")
      body = Json.stringify(HounsfieldRequest.serializer(), request)
    }
  }

  override suspend fun confirmCtTypeForResearch(
    token: String,
    request: ConfirmCTTypeRequest
  ): BaseResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/${request.researchId}/$MARK_ROUTE")
      body = Json.stringify(ConfirmCTTypeRequest.serializer(), request)
    }
  }

  override suspend fun closeSession(token: String, researchId: Int): BaseResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId/$CLOSE_ROUTE")
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