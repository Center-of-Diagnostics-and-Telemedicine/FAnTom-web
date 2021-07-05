package repository

import model.HounsfieldRequestNew
import model.init.ResearchInitModel
import model.SliceRequestNew

interface RemoteLibraryRepository {
  val libraryContainerId: String
  suspend fun initResearch(accessionNumber: String): ResearchInitModel
  suspend fun getSlice(request: SliceRequestNew, researchName: String): String
  suspend fun hounsfield(request: HounsfieldRequestNew): Double
  suspend fun closeSession()
}