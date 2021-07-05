package remote

import model.*
import model.fantom.FantomHounsfieldResponse
import model.fantom.FantomResearchInitResponse
import model.fantom.FantomSliceResponse

interface FantomLibraryDataSource {
  val endPoint: String
  val onClose: () -> Unit

  suspend fun getAccessionNames(): List<String>
  suspend fun initResearch(accessionNumber: String): FantomResearchInitResponse
  suspend fun getSlice(sliceRequest: SliceRequestNew, researchName: String): FantomSliceResponse
  suspend fun getHounsfield(request: HounsfieldRequestNew): FantomHounsfieldResponse
  suspend fun closeSession()
}