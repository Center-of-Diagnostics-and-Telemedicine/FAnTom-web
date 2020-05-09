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
  suspend fun initResearch(accessionNumber: String): ResearchInitResponse
  suspend fun getSlice(sliceRequest: SliceRequest, researchName: String): SliceResponse
  suspend fun getHounsfield(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): HounsfieldResponse
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
    install(HttpTimeout) {
      requestTimeoutMillis = 60000
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

  override suspend fun initResearch(accessionNumber: String): ResearchInitResponse {
    return client.get {
      apiUrl("$RESEARCH_ROUTE/$INIT_ROUTE/$accessionNumber")
    }
  }

  override suspend fun getSlice(
    sliceRequest: SliceRequest,
    researchName: String
  ): SliceResponse {
    return client.post {
      apiUrl("/$RESEARCH_ROUTE")
      body = Json.stringify(SliceRequest.serializer(), sliceRequest)
    }
  }

  override suspend fun getHounsfield(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): HounsfieldResponse {
    return client.get {
      apiUrl("$RESEARCH_ROUTE/$HOUNSFIELD_ROUTE")
      parameter(TYPE_AXIAL, axialCoord)
      parameter(TYPE_FRONTAL, frontalCoord)
      parameter(TYPE_SAGITTAL, sagittalCoord)
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