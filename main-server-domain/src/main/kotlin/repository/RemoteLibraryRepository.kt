package repository

import model.HounsfieldRequestNew
import model.ResearchInitModelNew
import model.SliceRequestNew

interface RemoteLibraryRepository {
  val libraryContainerId: String
  suspend fun initResearch(accessionNumber: String): ResearchInitModelNew
  suspend fun getSlice(request: SliceRequestNew, researchName: String): String
  suspend fun hounsfield(request: HounsfieldRequestNew): Double
  suspend fun closeSession()
}