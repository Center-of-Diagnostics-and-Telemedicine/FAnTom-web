package repository

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.*
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory
import fantom.FantomLibraryDataSourceImpl
import model.LOCALHOST
import model.data_store_path
import util.debugLog
import util.fantomServerPort
import java.io.File

interface LibraryCreator {
  suspend fun createLibrary(
    userId: Int,
    accessionNumber: String,
    port: Int,
    researchDir: File,
    onClose: () -> Unit
  ): RemoteLibraryRepository

  suspend fun deleteLibrary(containerId: String)
}

class LibraryCreatorImpl() : LibraryCreator {

  private val config = DefaultDockerClientConfig.createDefaultConfigBuilder()
    .withDockerHost("unix:///var/run/docker.sock")
    .withRegistryUsername("m.gusev")
    .withRegistryPassword("8vkWq8%T")
    .build()

  private val execFactory = JerseyDockerCmdExecFactory()
    .withReadTimeout(20000)
    .withConnectTimeout(20000)

  private val dockerClient: DockerClient = DockerClientImpl
    .getInstance(config)
    .withDockerCmdExecFactory(execFactory)

  override suspend fun createLibrary(
    userId: Int,
    accessionNumber: String,
    port: Int,
    researchDir: File,
    onClose: () -> Unit
  ): RemoteLibraryRepository {

    val createResponse = createContainerRequest(port, researchDir).exec()
    debugLog("call start container cmd")
    dockerClient.startContainerCmd(createResponse.id).exec()
    debugLog("call wait container cmd")
    dockerClient.waitContainerCmd(createResponse.id)
    return RemoteLibraryRepositoryImpl(
      remoteDataSource = FantomLibraryDataSourceImpl(
        endPoint = "$LOCALHOST:${port}",
        onClose = onClose
      ),
      libraryContainerId = createResponse.id
    )

  }

  private fun createContainerRequest(
    port: Int,
    researchDir: File
  ): CreateContainerCmd {

    val portBindings = Ports()
    val portInsideContainer = ExposedPort.tcp(fantomServerPort)
    val portOutsideContainer = Ports.Binding("0.0.0.0", port.toString())
    portBindings.bind(portInsideContainer, portOutsideContainer)

    val dirWithResearchInsideContainer = Volume("$data_store_path/${researchDir.name}")
    debugLog("research path = ${researchDir.path}, dirWithResearchInsideContainer = $dirWithResearchInsideContainer")

    val bindDir = Bind(researchDir.path, dirWithResearchInsideContainer)
    return dockerClient
      .createContainerCmd("fantom")
      .withCmd(
        "java",
        "-server",
        "-XX:+UnlockExperimentalVMOptions",
        "-XX:+UseCGroupMemoryLimitForHeap",
        "-XX:InitialRAMFraction=2",
        "-XX:MinRAMFraction=2",
        "-XX:MaxRAMFraction=2",
        "-XX:+UseG1GC",
        "-XX:MaxGCPauseMillis=100",
        "-XX:+UseStringDeduplication",
        "-jar",
        "backend-all.jar",
        "$port"
      )
      .withExposedPorts(portInsideContainer)
      .withPortBindings(portBindings)
      .withPublishAllPorts(true)
      .withVolumes(dirWithResearchInsideContainer)
      .withBinds(bindDir)
  }

  override suspend fun deleteLibrary(containerId: String) {
    dockerClient.stopContainerCmd(containerId).exec()
    dockerClient.waitContainerCmd(containerId).exec(WaitContainerResultCallback())
    debugLog("remove container id = $containerId")
    dockerClient.removeContainerCmd(containerId).exec()
  }

}