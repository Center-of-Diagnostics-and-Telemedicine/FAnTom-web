package client.datasource.remote

import model.*
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json

class ResearchRemoteDataSource : ResearchRemote {

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
  }

  override suspend fun getResearches(token: String): List<Research> {
    return client.get<ResearchesResponse> {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$LIST_ROUTE")
    }.researches
  }

  override suspend fun initResearch(token: String, researchId: Int): ResearchInitResponse {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$INIT_ROUTE/$researchId")
    }
  }

  override suspend fun getSlice(
    token: String,
    request: SliceRequest,
    researchId: Int
  ): String {
    return client.post {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$researchId")
      body = Json.stringify(SliceRequest.serializer(), request)
    }
  }

  override suspend fun createMark(token: String, request: NewMarkRequest): SelectedArea {
    return client.post {
      authHeader(token)
      apiUrl(MARK_ROUTE)
      body = Json.stringify(NewMarkRequest.serializer(), request)
    }
  }

  override suspend fun deleteMark(selectedArea: SelectedArea, token: String) {
    return client.delete {
      authHeader(token)
      apiUrl("$MARK_ROUTE/${selectedArea.id}")
    }
  }

  override suspend fun deleteMark(areaId: Int, token: String) {
    return client.delete {
      authHeader(token)
      apiUrl("$MARK_ROUTE/${areaId}")
    }
  }

  override suspend fun getMarks(researchId: Int, token: String): List<SelectedArea> {
    return client.get<MarksResponse> {
      authHeader(token)
      apiUrl("$MARK_ROUTE?research_id=$researchId")
    }.marks
  }

  override suspend fun updateMark(selectedArea: SelectedArea, token: String): SelectedArea {
    return client.put {
      authHeader(token)
      apiUrl("$MARK_ROUTE/${selectedArea.id}")
      body = Json.stringify(SelectedArea.serializer(), selectedArea)
    }
  }

  override suspend fun getHounsfieldData(
    token: String,
    request: HounsfieldRequest
  ): Double {
    return client.get<HounsfieldResponse> {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE?$TYPE_AXIAL=${request.axialCoord}&$TYPE_FRONTAL=${request.frontalCoord}&$TYPE_SAGITTAL=${request.sagittalCoord}")
    }.huValue
  }

  override suspend fun closeResearch(token: String, researchId: Int) {
    return client.get {
      authHeader(token)
      apiUrl("$RESEARCH_ROUTE/$CLOSE_ROUTE/$researchId")
    }
  }

  override suspend fun closeSession(token: String) {
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