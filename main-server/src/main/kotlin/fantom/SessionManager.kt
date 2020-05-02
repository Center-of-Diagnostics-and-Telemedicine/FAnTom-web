package fantom

import util.basePort
import util.debugLog
import kotlinx.coroutines.delay

object SessionManager {

  private val instances: MutableList<FantomLibrary> = mutableListOf()
  private val containers: MutableList<DockerContainer> = mutableListOf()

  suspend fun getInstanceForUser(userId: Int): FantomLibrary {
    val existing = instances.firstOrNull { it.userId == userId }
    return if (existing == null) {

      debugLog("create fantomLibrary")
      val instance = FantomLibraryImpl(
        userId = userId,
        endPoint = "http://127.0.0.1",
        container = getContainer(userId),
        onClose = { container ->
          DockerManager.close(container = container)
          containers.remove(container)
          instances.remove(
            instances.firstOrNull {
              it.container.containerId == container.containerId
            })
        }
      )

      instances.add(instance)

      //Ставим delay, чтобы успела инициализироваться библиотека
      delay(2000)
      instance
    } else {
      debugLog("using existing instance")
      existing
    }
  }

  private fun getContainer(userId: Int): DockerContainer {
    val existing = containers.firstOrNull { it.userId == userId }
    return if (existing == null) {
      debugLog("create container")
      val container = DockerManager.create(userId, basePort + containers.size + 1)
      containers.add(container)
      container
    } else {
      debugLog("using existing container")
      existing
    }
  }


}