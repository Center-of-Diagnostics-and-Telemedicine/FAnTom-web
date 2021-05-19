package repository

import kotlinx.coroutines.CoroutineScope

interface SessionRepository : CoroutineScope {
  suspend fun getSession(userId: Int): RemoteLibraryRepository?
  suspend fun createSession(userId: Int, accessionNumber: String): RemoteLibraryRepository
  suspend fun deleteSession(userId: Int)
}
