package repository

import ContainerCreatorImpl
import kotlin.coroutines.CoroutineContext

class SessionRepositoryFactory(
  private val dockerHost: String,
  private val dockerUserName: String,
  private val dockerUserPassword: String,
  private val researchDirFinder: ResearchDirFinder,
  private val context: CoroutineContext
) {

  fun build(): SessionRepository {
    val creator = ContainerCreatorImpl(
      dockerHost = dockerHost,
      dockerUserName = dockerUserName,
      dockerUserPassword = dockerUserPassword
    )

    return SessionRepositoryImpl(
      creator,
      researchDirFinder,
      context
    )
  }
}