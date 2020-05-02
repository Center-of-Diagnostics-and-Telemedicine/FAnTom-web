package fantom

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.WaitContainerResultCallback
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.Ports
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory
import util.debugLog
import util.fantomServerPort

object DockerManager {

  private val dockerClient: DockerClient = DockerClientImpl
    .getInstance(
      DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("unix:///var/run/docker.sock")
        .withRegistryUsername("max")
        .withRegistryPassword(" ")
        .build()
    ).withDockerCmdExecFactory(
      JerseyDockerCmdExecFactory()
        .withReadTimeout(1000)
        .withConnectTimeout(1000)
    )

  fun create(userId: Int, port: Int): DockerContainer {

    //порт который будем открывать в контейнере
    val portInsideContainer = ExposedPort.tcp(port)
    val portBindings = Ports()
    //порт, который будем выдавать нашей программе. Его надо прокидывать через кмд
    portBindings.bind(portInsideContainer, Ports.Binding("0.0.0.0", fantomServerPort.toString()))

    val createResponse = dockerClient
      .createContainerCmd("fantom")
      .withCmd(
        "java",
        "-server",
        "-Djna.debug_load=true",
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
      .exec()

    debugLog("start container id = ${createResponse.id}")
    dockerClient.startContainerCmd(createResponse.id).exec()
    return DockerContainer(createResponse.id, userId, port)
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
  val port: Int
)