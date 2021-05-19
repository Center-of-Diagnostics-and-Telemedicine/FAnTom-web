import repository.ResearchDirFinder
import java.io.File

class ResearchDirFinderImpl(
  override val rootDirPath: String
) : ResearchDirFinder {

  override fun getResearchPath(accessionName: String): String {

    val listFiles = File(rootDirPath).listFiles()
    if (listFiles != null) {
      for (file in listFiles) {
        if (file.isDirectory) {
          if (file.name.contains(accessionName)) {
            debugLog("file contains $accessionName")
            return file.path
          }
        } else if (file.isFile) {
          if (file.name.contains(accessionName)) {
            debugLog("file contains $accessionName")
            return file.path
          }
        }
      }
    }

    throw NoSuchElementException("cant find dir for research: $accessionName")
  }
}