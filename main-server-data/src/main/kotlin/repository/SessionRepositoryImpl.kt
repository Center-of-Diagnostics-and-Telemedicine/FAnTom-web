package repository

import debugLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.LOCALHOST
import remote.FantomLibraryDataSourceImpl
import kotlin.coroutines.CoroutineContext

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
    val researchDir = researchDirFinder.getResearchPath(accessionNumber)
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