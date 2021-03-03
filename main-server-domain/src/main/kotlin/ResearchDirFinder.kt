package repository

import java.io.File

interface ResearchDirFinder {

  val rootDirPath: String

  fun getResearchPath(accessionName: String, rootDir: File = File(rootDirPath)): File
}
