package fantom

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.*
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory
import util.debugLog
import util.fantomServerPort
import java.io.File


object DockerManager {

  private val dockerClient: DockerClient = DockerClientImpl
    .getInstance(
      DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("unix:///var/run/docker.sock")
        .withRegistryUsername("m.gusev")
        .withRegistryPassword("8vkWq8%T")
        .build()
    ).withDockerCmdExecFactory(
      JerseyDockerCmdExecFactory()
        .withReadTimeout(20000)
        .withConnectTimeout(20000)
    )

  fun create(userId: Int, port: Int, researchDir: File): DockerContainer? {

    //порт который будем открывать в контейнере
    val portInsideContainer = ExposedPort.tcp(fantomServerPort)
    val portBindings = Ports()
    //порт, который будем выдавать нашей программе. Его надо прокидывать через кмд
    portBindings.bind(portInsideContainer, Ports.Binding("0.0.0.0", port.toString()))

    val volume1 = Volume("/app/dicom/${researchDir.name}")

    debugLog("going create container")

    val createResponse = dockerClient
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
        "backend.jar",
        "$port"
      )
      .withExposedPorts(portInsideContainer)
      .withPortBindings(portBindings)
      .withPublishAllPorts(true)
      .withVolumes(volume1)
      .withBinds(Bind(researchDir.path, volume1))
      .exec()

    debugLog("start container id = ${createResponse.id}, path = ${researchDir.path}")
    val container = DockerContainer(
      containerId = createResponse.id,
      userId = userId,
      port = port,
      dirName = researchDir.name
    )
    try {
      dockerClient.startContainerCmd(createResponse.id).exec()
    } catch (e: Exception) {
      e.printStackTrace()
      debugLog("exception, going to close container")
      close(container)
      return null
    }
    return container
  }

  fun close(container: DockerContainer) {
    dockerClient.stopContainerCmd(container.containerId).exec()
    dockerClient.waitContainerCmd(container.containerId).exec(WaitContainerResultCallback())
    debugLog("remove container id = ${container.containerId}")
    dockerClient.removeContainerCmd(container.containerId).exec()
  }

}

data class DockerContainer(
  val containerId: String,
  val userId: Int,
  val port: Int,
  val dirName: String
)