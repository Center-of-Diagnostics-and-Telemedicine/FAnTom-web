package repository

interface ResearchDirFinder {

  val rootDirPath: String

  fun getResearchPath(accessionName: String): String
}
