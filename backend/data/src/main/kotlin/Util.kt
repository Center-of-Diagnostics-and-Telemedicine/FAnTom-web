fun Any.debugLog(text: String) {
  println("${this.javaClass.simpleName.uppercase()}: $text")
}