import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import presentation.app.app
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window
import kotlin.coroutines.CoroutineContext

private class Application : CoroutineScope {
  override val coroutineContext: CoroutineContext = Job()

  fun start() {
    window.onload = {
      render(document.getElementById("app")) {
        app()
      }
    }
  }
}

fun main() {
  GlobalStyles.inject()

  Application().start()
}

fun Any.debugLog(text: String?) {
  if (text.isNullOrEmpty().not()) console.log("${this::class.simpleName?.toUpperCase()}: $text")
}