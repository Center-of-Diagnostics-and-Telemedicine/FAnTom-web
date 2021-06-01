package components.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.extensions.reaktive.labels
import com.badoo.reaktive.observable.mapNotNull
import com.badoo.reaktive.observable.subscribe
import components.asValue
import components.getStore
import components.login.Login.Dependencies
import components.login.Login.Model
import store.login.LoginStore
import store.login.LoginStore.Intent

internal class LoginComponent(
  componentContext: ComponentContext,
  dependencies: Dependencies
) : Login, ComponentContext by componentContext, Dependencies by dependencies {

  private val store: LoginStore = instanceKeeper.getStore {
    LoginStoreProvider(
      storeFactory = storeFactory,
      repository = dependencies.loginRepository
    ).provide()
  }

  init {
    store.labels.mapNotNull(labelsToOutput).subscribe(onNext = loginOutput::onNext)
  }

  override val model: Value<Model> = store.asValue().map(stateToModel)

  override fun auth() {
    store.accept(Intent.Auth)
  }

  override fun onPasswordChanged(password: String) {
    store.accept(Intent.HandlePasswordChanged(password))
  }

  override fun onLoginChanged(login: String) {
    store.accept(Intent.HandleLoginChanged(login))
  }

}