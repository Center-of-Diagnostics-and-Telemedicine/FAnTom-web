package repository

import model.HounsfieldRequestNew
import model.ResearchInitModel
import model.ResearchInitModelNew
import model.SliceRequestNew
import model.fantom.FantomResearchInitModel

interface RemoteLibraryRepository {
  val libraryContainerId: String
  suspend fun initResearch(accessionNumber: String): ResearchInitModel
  suspend fun getSlice(request: SliceRequestNew, researchName: String): String
  suspend fun hounsfield(request: HounsfieldRequestNew): Double
  suspend fun closeSession()
}