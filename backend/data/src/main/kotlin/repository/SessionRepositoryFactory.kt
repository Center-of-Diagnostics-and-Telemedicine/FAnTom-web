package repository

import ContainerCreatorImpl
import model.DockerConfigModel
import kotlin.coroutines.CoroutineContext

fun SessionRepositoryFactory(
  dockerConfigModel: DockerConfigModel,
  researchDirFinder: ResearchDirFinder,
  context: CoroutineContext,
): SessionRepository {
  val creator = ContainerCreatorImpl(dockerConfigModel)

  return SessionRepositoryImpl(
    creator,
    researchDirFinder,
    context
  )
}