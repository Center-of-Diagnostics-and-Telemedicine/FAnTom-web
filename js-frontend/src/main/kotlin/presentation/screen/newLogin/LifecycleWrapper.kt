package presentation.screen.newLogin

import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry

class LifecycleWrapper {

  val lifecycle = LifecycleRegistry()

  fun start() {
    lifecycle.onCreate()
    lifecycle.onStart()
    lifecycle.onResume()
  }

  fun stop() {
    lifecycle.onPause()
    lifecycle.onStop()
    lifecycle.onStop()
  }
}