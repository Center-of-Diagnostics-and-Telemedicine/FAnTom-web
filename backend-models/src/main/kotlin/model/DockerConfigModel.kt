package model

data class DockerConfigModel(
  val dockerHost: String,
  val dockerUserName: String,
  val dockerUserPassword: String,
  val dockerDataStorePath: String,
  val dockerContainerAppConfigModel: DockerContainerAppConfigModel
)

data class DockerContainerAppConfigModel(
  val port: Int,
  val name: String,
  val mainFile: String,
  val configFile: String
)