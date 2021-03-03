import repository.ResearchDirFinder
import java.io.File

class ResearchDirFinderImpl(
  override val rootDirPath: String
) : ResearchDirFinder {

  override fun getResearchPath(accessionName: String, rootDir: File): File {

    val listFiles = rootDir.listFiles()
    if (listFiles != null) {
      for (file in listFiles) {
        if (file.isDirectory) {
          if (file.name.contains(accessionName)) {
            debugLog("file contains $accessionName")
            return file
          }
        } else if (file.isFile) {
          if (file.name.contains(accessionName)) {
            debugLog("file contains $accessionName")
            return file
          }
        }
      }
    }

    throw NoSuchElementException("cant find dir for research: $accessionName")
  }
}