import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry

fun LifecycleRegistry.resume() {
  if (state == Lifecycle.State.INITIALIZED) {
    onCreate()
  }

  if (state == Lifecycle.State.CREATED) {
    onStart()
  }

  if (state == Lifecycle.State.STARTED) {
    onResume()
  }
}

fun LifecycleRegistry.stop() {
  if (state == Lifecycle.State.INITIALIZED) {
    onCreate()
  } else {
    if (state == Lifecycle.State.RESUMED) {
      onPause()
    }

    if (state == Lifecycle.State.STARTED) {
      onStop()
    }
  }
}

fun LifecycleRegistry.destroy() {
  stop()

  if (state == Lifecycle.State.CREATED) {
    onDestroy()
  }
}
