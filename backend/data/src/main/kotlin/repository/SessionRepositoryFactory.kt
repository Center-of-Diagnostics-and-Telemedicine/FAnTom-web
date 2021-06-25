package repository

import ContainerCreatorImpl
import model.DockerConfigModel
import repository.repository.SessionRepository
import kotlin.coroutines.CoroutineContext

class SessionRepositoryFactory(
  private val dockerConfigModel: DockerConfigModel,
  private val researchDirFinder: ResearchDirFinder,
  private val context: CoroutineContext,
) {

  fun build(): SessionRepository {
    val creator = ContainerCreatorImpl(dockerConfigModel)

    return SessionRepositoryImpl(
      creator,
      researchDirFinder,
      context
    )
  }
}