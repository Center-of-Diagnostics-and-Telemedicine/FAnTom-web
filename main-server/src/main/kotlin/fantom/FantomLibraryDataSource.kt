package fantom

import com.badoo.reaktive.observable.debounce
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.subject.publish.PublishSubject
import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json
import model.*
import util.debugLog

interface FantomLibraryDataSource {
  val endPoint: String
  val onClose: () -> Unit

  suspend fun getAccessionNames(): List<String>
  suspend fun initResearch(accessionNumber: String): ApiResponse
  suspend fun getSlice(sliceRequest: SliceRequest, researchName: String): ApiResponse
  suspend fun getHounsfield(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): ApiResponse
}

class FantomLibraryDataSourceImpl(
  override val endPoint: String,
  override val onClose: () -> Unit
) : FantomLibraryDataSource {

  private val sessionDebounceSubject = PublishSubject<Boolean>()

  private val client: HttpClient = HttpClient {
    install(JsonFeature) {
      serializer = KotlinxSerializer()
    }
  }

  init {
    sessionDebounceSubject
      .debounce(tenMinutes, computationScheduler)
      .subscribe { container ->
        debugLog("going to close container")
        onClose()
      }
  }

  override suspend fun getAccessionNames(): List<String> {
    return client.get<AccessionNamesResponse> {
      apiUrl("$RESEARCH_ROUTE/$LIST_ROUTE")
    }.let {
      debugLog("size of accessions = ${it.accessionNames.size}, ${it.accessionNames}")
      it.accessionNames
    }
  }

  override suspend fun initResearch(accessionNumber: String): ApiResponse {
    return client.get {
      apiUrl("$RESEARCH_ROUTE/$INIT_ROUTE?id=$accessionNumber")
    }
  }

  override suspend fun getSlice(sliceRequest: SliceRequest, researchName: String): ApiResponse {
    return client.post {
      apiUrl(researchName)
      body = Json.stringify(SliceRequest.serializer(), sliceRequest)
    }
  }

  override suspend fun getHounsfield(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): ApiResponse {
    return client.get<ApiResponse.HounsfieldResponse> {
      apiUrl("$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE?$TYPE_AXIAL=${axialCoord}&$TYPE_FRONTAL=${frontalCoord}&$TYPE_SAGITTAL=${sagittalCoord}")
    }
  }

  private fun HttpRequestBuilder.apiUrl(path: String) {
    sessionDebounceSubject.onNext(true)
    url {
      takeFrom(endPoint)
      encodedPath = path
    }
  }

}