package repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import util.data_store_paths

interface SessionRepository {
  suspend fun getSession(userId: Int): RemoteLibraryRepository?
  suspend fun createSession(userId: Int, accessionNumber: String): RemoteLibraryRepository
  suspend fun deleteSession(userId: Int, accessionNumber: String)
}

class SessionRepositoryImpl(
  private val creator: LibraryCreator,
  private val researchDirFinder: ResearchDirFinder
) : SessionRepository {

  private val sessions: MutableMap<Int, RemoteLibraryRepository> = mutableMapOf()
  private val portsCounter: Int = 0

  override suspend fun getSession(userId: Int): RemoteLibraryRepository? {
    return sessions[userId]
  }

  override suspend fun createSession(
    userId: Int,
    accessionNumber: String
  ): RemoteLibraryRepository {
    val researchDir = researchDirFinder.getResearchPath(accessionNumber, data_store_paths)
    val port = portsCounter + 1


    val library = creator
      .createLibrary(
        userId = userId,
        accessionNumber = accessionNumber,
        port = port,
        researchDir = researchDir,
        onClose = {
          GlobalScope.launch { deleteSession(userId, accessionNumber) }
        }
      )
    sessions[userId] = library
    return library

  }

  override suspend fun deleteSession(userId: Int, accessionNumber: String) {
    val session = sessions[userId]
      ?: throw IllegalStateException("library for user: $userId not found")

    creator.deleteLibrary(session.libraryContainerId)
    sessions.remove(userId)
  }

}