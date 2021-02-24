package repository


import java.io.File

interface ContainerCreator {
  suspend fun createContainer(
    userId: Int,
    accessionNumber: String,
    port: Int,
    researchDir: File,
    onClose: () -> Unit
  ): String

  suspend fun deleteLibrary(containerId: String)
}
