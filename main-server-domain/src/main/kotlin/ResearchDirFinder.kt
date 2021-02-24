package repository

import java.io.File

interface ResearchDirFinder {
  fun getResearchPath(accessionName: String, rootDir: File): File
}
