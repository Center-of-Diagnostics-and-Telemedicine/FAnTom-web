package util

fun Any.debugLog(text: String) {
  println("${this.javaClass.simpleName.toUpperCase()}: $text")
}

fun parseInt(number: String): Int {
  return try {
    number.toInt()
  } catch (e: Throwable) {
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