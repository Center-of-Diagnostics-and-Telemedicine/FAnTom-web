package repository

interface SessionRepository {
  suspend fun getSession(userId: Int, accessionNumber: String): RemoteLibraryRepository?
  suspend fun createSession(userId: Int, accessionNumber: String): RemoteLibraryRepository
  suspend fun deleteSession(userId: Int, accessionNumber: String)
}

class SessionRepositoryImpl(): SessionRepository{
  override suspend fun getSession(userId: Int, accessionNumber: String): RemoteLibraryRepository? {
    TODO("Not yet implemented")
  }

  override suspend fun createSession(
    userId: Int,
    accessionNumber: String
  ): RemoteLibraryRepository {
    TODO("Not yet implemented")
  }

  override suspend fun deleteSession(userId: Int, accessionNumber: String) {
    TODO("Not yet implemented")
  }

}