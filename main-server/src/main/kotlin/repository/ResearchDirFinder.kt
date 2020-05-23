package repository

import java.io.File

interface ResearchDirFinder {
  fun getResearchPath(accessionName: String, rootDir: File): File
}

class ResearchDirFinderImpl : ResearchDirFinder {

  override fun getResearchPath(accessionName: String, rootDir: File): File {

    val listFiles = rootDir.listFiles()
    if (listFiles != null) {
      for (file in listFiles) {
        if (file.isDirectory) {
          if (file.name.contains(accessionName)) {
            println("file contains $accessionName")
            return file
          }
        }
      }
    }

    throw NoSuchElementException("cant find dir for research: $accessionName")
  }
}
