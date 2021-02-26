package util

import model.libraryServerDomain
import model.libraryServerPort
import model.libraryServerSchema
import model.localDataStorePath
import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options

//сервер может работать с java library
//сервер может работать с qt library

//сервер может запускать docker
//сервер может работать без docker

data class Config(
  val libraryServerDomain: String,
  val libraryServerPort: Int,
  val dataStorePath: String
)

fun parseConfig(args: Array<String>): Config {
  val options = Options()

  val serverUrlInput = Option.builder("u").build()
  options.addOption(serverUrlInput)

  val serverPortInput = Option.builder("p").build()
  options.addOption(serverPortInput)

  val storePath = Option.builder("d").build()
  options.addOption(storePath)

  val parser: CommandLineParser = DefaultParser()
  val cmd = parser.parse(options, args)

  val portInCmd = cmd.getOptionValue("p")
  val urlInCmd = cmd.getOptionValue("u")
  val urlInCdataStorePathInCmd = cmd.getOptionValue("d")

  return Config(
    libraryServerPort = portInCmd?.toInt() ?: libraryServerPort,
    libraryServerDomain = urlInCmd ?: libraryServerSchema.plus(libraryServerDomain),
    dataStorePath = urlInCdataStorePathInCmd ?: localDataStorePath
  )
}
