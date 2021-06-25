package repository.repository

import model.*

interface FantomLibraryDataSource {
  val endPoint: String
  val onClose: () -> Unit

  suspend fun getAccessionNames(): List<String>
  suspend fun initResearch(accessionNumber: String): ResearchInitResponseNew
  suspend fun getSlice(sliceRequest: SliceRequestNew, researchName: String): SliceResponse
  suspend fun getHounsfield(request: HounsfieldRequestNew): HounsfieldResponse
  suspend fun closeSession()
}