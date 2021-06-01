package components.login

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import components.login.Login.Dependencies
import repository.LoginRepository

interface Login {

  val model: Value<Model>

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
    val loginOutput: Consumer<Output>
    val loginRepository: LoginRepository
  }

  sealed class Output {
    object Authorized : Output()
  }
}

@Suppress("FunctionName") // Factory function
fun Login(componentContext: ComponentContext, dependencies: Dependencies): Login =
  LoginComponent(componentContext, dependencies)