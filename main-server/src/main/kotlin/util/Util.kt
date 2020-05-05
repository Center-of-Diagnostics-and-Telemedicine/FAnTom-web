package util

import java.io.File


fun Any.debugLog(text: String) {
  println("${this.javaClass.simpleName.toUpperCase()}: $text")
}

fun parseInt(number: String): Int {
  return try {
    number.toInt()
  } catch (e: NumberFormatException) {
    Int.MIN_VALUE
  }
}

fun parseDouble(number: String): Double {
  return try {
    number.toDouble()
  } catch (e: Exception) {
    Double.MIN_VALUE
  }
}

fun getResearchPath(
  name: String,
  rootDirs: List<File>
): File? {
//  debugLog("going to search for researchDir")

  rootDirs.forEach { rootDir ->
    //    debugLog("root research dir = $rootDir")
    val listFiles = rootDir.listFiles()
    if (listFiles != null) {
//      debugLog("listFiles != null")
      for (file in listFiles) {
//        debugLog("trying file = ${file.name}")
        if (file.isDirectory) {
//          debugLog("file is dir")
//          debugLog("filename.contains(name, ignoreCase)=${file.nameWithoutExtension.contains(name, true)}")
//          debugLog("filename.contains(name)=${file.nameWithoutExtension.contains(name)}")
          if (file.name.contains(name)) {
            println("file contains $name")
            return file
          }
        }
      }
    }
  }

  return null
}