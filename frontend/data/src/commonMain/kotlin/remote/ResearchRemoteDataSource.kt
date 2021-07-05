package remote

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import model.*
import model.init.ResearchInitResponse
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
  ): ResearchInitResponse {
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
      body = Json.encodeToString(SliceRequestNew.serializer(), request)
    }
  }

  override suspend fun hounsfield(
    token: String,
    request: HounsfieldRequestNew
  ): HounsfieldResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE")
      body = Json.encodeToString(HounsfieldRequestNew.serializer(), request)
    }
  }

  override suspend fun confirmCtTypeForResearch(
    token: String,
    request: ConfirmCTTypeRequest
  ): BaseResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/${request.researchId}/$MARK_ROUTE")
      body = Json.encodeToString(ConfirmCTTypeRequest.serializer(), request)
    }
  }

  override suspend fun closeSession(token: String, researchId: Int): BaseResponse {
    return client.get {
      authHeader(token)
      apiUrl("$SESSION_ROUTE/$CLOSE_ROUTE")
    }
  }

  override suspend fun closeResearch(token: String, researchId: Int): BaseResponse {
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
      takeFrom(MAIN_SERVER_URL)
      encodedPath = path
    }
  }


}