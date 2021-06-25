package repository


interface ContainerCreator {
  suspend fun createContainer(
    userId: Int,
    accessionNumber: String,
    port: Int,
    researchDir: String,
    onClose: () -> Unit
  ): String

  suspend fun deleteLibrary(containerId: String)
}
