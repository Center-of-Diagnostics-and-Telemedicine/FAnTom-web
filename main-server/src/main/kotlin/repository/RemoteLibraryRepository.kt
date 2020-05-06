package repository

import model.ResearchInitResponse
import model.SliceRequest

interface RemoteLibraryRepository{
  suspend fun initResearch(accessionNumber: String)
  suspend fun getResearchData(accessionNumber: String): ResearchInitResponse

  suspend fun createSession(userId: Int, accessionNumber: String)
  suspend fun deleteSession(userId: Int, accessionNumber: String)
  suspend fun getSlice(request: SliceRequest): String
  fun hounsfield(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int)
}

class RemoteLibraryRepositoryImpl()