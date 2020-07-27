package repository

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.Bind
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.Ports
import com.github.dockerjava.api.model.Volume
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory
import model.dockerDataStorePath
import model.libraryServerPort
import util.debugLog
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

class ContainerCreatorImpl() : ContainerCreator {

  private val config = DefaultDockerClientConfig.createDefaultConfigBuilder()
//    .withDockerHost("unix:///var/run/docker.sock")
//    .withRegistryUsername("max")
//    .withRegistryPassword("vfrcbv16")
        .withDockerHost("unix:///var/run/docker.sock")
    .withRegistryUsername("m.gusev")
    .withRegistryPassword("8vkWq8%T")
//    .withDockerHost("tcp://localhost:2375")
//    .withRegistryUsername("m.gusev")
//    .withRegistryPassword("Gusev!8")
    .build()

  private val execFactory = JerseyDockerCmdExecFactory()
    .withReadTimeout(20000)
    .withConnectTimeout(20000)

  private val dockerClient: DockerClient = DockerClientImpl
    .getInstance(config)
    .withDockerCmdExecFactory(execFactory)

  override suspend fun createContainer(
    userId: Int,
    accessionNumber: String,
    port: Int,
    researchDir: File,
    onClose: () -> Unit
  ): String {

    val createResponse = createContainerRequest(port, researchDir).exec()
    debugLog("call start container cmd")
    dockerClient.startContainerCmd(createResponse.id).exec()
    debugLog("call wait container cmd")
    dockerClient.waitContainerCmd(createResponse.id)
    return createResponse.id

  }

  private fun createContainerRequest(
    port: Int,
    researchDir: File
  ): CreateContainerCmd {

    val portBindings = Ports()
    val portInsideContainer = ExposedPort.tcp(libraryServerPort)
    val portOutsideContainer = Ports.Binding("0.0.0.0", port.toString())
    portBindings.bind(portInsideContainer, portOutsideContainer)

    val researchPath = "${dockerDataStorePath}/${researchDir.name}"
    val dirWithResearchInsideContainer = Volume(researchPath)
    debugLog("research path = ${researchDir.path}, dirWithResearchInsideContainer = $dirWithResearchInsideContainer")

    val bindDir = Bind(researchDir.path, dirWithResearchInsideContainer)
    return dockerClient
      .createContainerCmd("fantom")
      .withCmd(
        "./FantomWebServer",
        researchPath,
        "/app/webserver.ini"
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
