package client.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import model.*

class ResearchRemoteDataSource : ResearchRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
    install(HttpTimeout) {
      requestTimeoutMillis = 60000
    }
  }

  override suspend fun getResearches(token: String): ApiResponse {
    return client.get<ApiResponse/*ResearchesResponse*/> {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$LIST_ROUTE")
    }
  }

  override suspend fun initResearch(
    token: String,
    researchId: Int
  ): ApiResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$INIT_ROUTE/$researchId")
    }
  }

  override suspend fun getSlice(
    token: String,
    request: SliceRequest,
    researchId: Int
  ): ApiResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId")
      body = Json.stringify(SliceRequest.serializer(), request)
    }
  }

  override suspend fun getHounsfieldData(
    token: String,
    request: HounsfieldRequest
  ): ApiResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE?$TYPE_AXIAL=${request.axialCoord}&$TYPE_FRONTAL=${request.frontalCoord}&$TYPE_SAGITTAL=${request.sagittalCoord}")
    }
  }

  override suspend fun confirmCtTypeForResearch(
    token: String,
    request: ConfirmCTTypeRequest
  ): ApiResponse {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/${request.researchId}/$MARK_ROUTE")
      body = Json.stringify(ConfirmCTTypeRequest.serializer(), request)
    }
  }

  override suspend fun closeSession(token: String): ApiResponse {
    return client.get {
      authHeader(token)
      apiUrl("$SESSION_ROUTE/$CLOSE_ROUTE")
    }
  }

  private fun HttpRequestBuilder.authHeader(token: String) {
    header("Authorization", "Bearer $token")
  }

  private fun HttpRequestBuilder.apiUrl(path: String) {
    url {
      takeFrom(END_POINT)
      encodedPath = path
    }
  }


}