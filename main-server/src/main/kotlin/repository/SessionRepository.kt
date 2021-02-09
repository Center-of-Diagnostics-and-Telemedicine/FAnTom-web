package repository

import ContainerCreator
import fantom.FantomLibraryDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.LOCALHOST
import model.localDataStorePath
import util.debugLog
import java.io.File
import kotlin.coroutines.CoroutineContext

interface SessionRepository : CoroutineScope {
  suspend fun getSession(userId: Int): RemoteLibraryRepository?
  suspend fun createSession(userId: Int, accessionNumber: String): RemoteLibraryRepository
  suspend fun deleteSession(userId: Int)
}

class SessionRepositoryImpl(
  private val creator: ContainerCreator,
  private val researchDirFinder: ResearchDirFinder,
  override val coroutineContext: CoroutineContext
) : SessionRepository {

  private val sessions: MutableMap<Int, RemoteLibraryRepository> = mutableMapOf()
  private var portsCounter: Int = 30000

  override suspend fun getSession(userId: Int): RemoteLibraryRepository? {
    return sessions[userId]
  }

  override suspend fun createSession(
    userId: Int,
    accessionNumber: String
  ): RemoteLibraryRepository {
    val researchDir = researchDirFinder.getResearchPath(accessionNumber, File(localDataStorePath))
    portsCounter += 1

    val containerId = creator
      .createContainer(
        userId = userId,
        accessionNumber = accessionNumber,
        port = portsCounter,
        researchDir = researchDir,
        onClose = {
          GlobalScope.launch {
            debugLog("call deleteSession")
            deleteSession(userId)
          }
        }
      )

    val library = RemoteLibraryRepositoryImpl(
      remoteDataSource = FantomLibraryDataSourceImpl(
        endPoint = "$LOCALHOST:${portsCounter}",
        onClose = {
          GlobalScope.launch {
            debugLog("call deleteSession")
            deleteSession(userId)
          }
        }
      ),
      libraryContainerId = containerId
    )

    sessions[userId] = library

    return library
  }

  override suspend fun deleteSession(userId: Int) {
    val session = sessions[userId]
      ?: throw IllegalStateException("library for user: $userId not found")
    session.closeSession()
    creator.deleteLibrary(session.libraryContainerId)
    sessions.remove(userId)
  }

}
