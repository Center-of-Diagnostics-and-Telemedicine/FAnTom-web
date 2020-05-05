package fantom

import kotlinx.coroutines.delay
import model.User
import util.*

object SessionManager {

  private val instances: MutableList<FantomLibrary> = mutableListOf()
  private val containers: MutableList<DockerContainer> = mutableListOf()
  private val ports: MutableList<Int> = mutableListOf(basePort)

  suspend fun getInstanceForUser(userId: Int, researchName: String): FantomLibrary? {
    val existing = instances.firstOrNull { it.userId == userId && it.researchName == researchName }
    return if (existing == null) {

      debugLog("create fantomLibrary")
      val container = getContainer(userId, researchName)
      if (container != null) {
        val instance = FantomLibraryImpl(
          userId = userId,
          endPoint = "http://127.0.0.1",
          container = container,
          researchName = researchName,
          onClose = { dockerContainer ->
            closeSession(dockerContainer)
          }
        )

        instances.add(instance)

        //Ставим delay, чтобы успела инициализироваться библиотека
        debugLog("instance created")
        delay(2000)

        instance
      } else {
        return null
      }
    } else {
      debugLog("using existing instance")
      existing
    }
  }

  fun closeSession(user: User) {
    instances
      .firstOrNull {
        it.userId == user.id
      }
      ?.let {
        closeSession(it.container)
      }
  }

  private fun closeSession(dockerContainer: DockerContainer) {
    DockerManager.close(container = dockerContainer)
    containers.remove(dockerContainer)
    instances.remove(
      instances.firstOrNull {
        it.container.containerId == dockerContainer.containerId
      })
  }

  private fun getContainer(userId: Int, researchName: String): DockerContainer? {
    val existing = containers.firstOrNull { it.userId == userId && it.researchName == researchName }
    return if (existing == null) {
      debugLog("create container for researchName = $researchName")
      val researchDir = getResearchPath(researchName.replace(" ", "").trim(), data_store_paths)
      if (researchDir == null) {
        throw NoSuchElementException("path for research = $researchName not found")
      } else {
        val port = ports.size + 1
        ports.add(port)
        debugLog("path found, path for research = ${researchDir.name}, port to add = $port")
        val container = DockerManager.create(
          userId = userId,
          port = port,
          researchDir = researchDir,
          researchName = researchName
        ) ?: return null
        containers.add(container)
        container
      }
    } else {
      debugLog("using existing container")
      existing
    }
  }


}