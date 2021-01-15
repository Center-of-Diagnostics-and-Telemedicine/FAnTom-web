package client.newmvi.login.binder

import client.newmvi.login.store.LoginStore
import client.newmvi.login.view.LoginView
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.subscribe

class LoginBinder(
  private val store: LoginStore
) {

  private var disposables = CompositeDisposable()
  private var view: LoginView? = null

  fun attachView(view: LoginView) {
    this.view = view
  }

  fun onStart() {
    disposables.add(
      requireNotNull(view)
        .events
        .map(LoginViewEventToIntentMapper::invoke)
        .subscribe(onNext = store::accept)
    )

    disposables.add(
      store
        .states
        .map(LoginStateToViewModelMapper::invoke)
        .subscribe(onNext = { requireNotNull(view).show(it) })
    )
  }

  fun onStop() {
    disposables.clear()
  }

  fun detachView() {
    view = null
  }

  fun onDestroy() {
    store.dispose()
  }
}