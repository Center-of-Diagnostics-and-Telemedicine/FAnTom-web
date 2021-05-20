package components.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.login.Login.Dependencies
import controller.LoginController
import repository.LoginRepository

interface Login {

  val models: Value<Model>

  fun auth()

  fun onPasswordChanged(password: String)

  fun onLoginChanged(login: String)

  data class Model(
    val login: String,
    val password: String,
    val error: String,
    val loading: Boolean
  )

  interface Dependencies {
    val storeFactory: StoreFactory
    val mainOutput: Consumer<Output>
    val repository: LoginRepository
  }

  sealed class Output {
    object Authorized : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun Login(componentContext: ComponentContext, dependencies: Dependencies): Login =
  LoginComponent(componentContext, dependencies)