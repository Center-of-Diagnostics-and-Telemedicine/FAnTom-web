package model

data class DockerConfigModel(
  val dockerHost: String,
  val dockerUserName: String,
  val dockerUserPassword: String,
  val dockerDataStorePath: String,
)