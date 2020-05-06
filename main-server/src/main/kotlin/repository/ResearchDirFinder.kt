package repository

import java.io.File

interface ResearchDirFinder {
  fun getResearchPath(accessionName: String, rootDirs: List<File>): File
}

class ResearchDirFinderImpl() : ResearchDirFinder {

  override fun getResearchPath(accessionName: String, rootDirs: List<File>): File {

    rootDirs.forEach { rootDir ->
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
    }

    throw NoSuchElementException("cant find dir for research: $accessionName")
  }
}