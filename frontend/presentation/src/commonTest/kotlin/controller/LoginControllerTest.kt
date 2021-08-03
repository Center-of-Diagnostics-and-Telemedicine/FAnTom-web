package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import controller.LoginController.Dependencies
import controller.LoginController.Output
import invalidLogin
import invalidPassword
import model.INVALID_AUTH_CREDENTIALS
import repository.LoginRepository
import repository.TestLoginRepository
import resume
import testLogin
import testPassword
import view.LoginView
import view.TestLoginView
import kotlin.test.*

class LoginControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<Output>()

  private val dependencies =
    object : Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory()
      override val lifecycle: Lifecycle = this@LoginControllerTest.lifecycle
      override val loginRepository: LoginRepository = TestLoginRepository()
      override val loginOutput: (Output) -> Unit = { output += it }
    }

  private val loginView = TestLoginView()
  private lateinit var controller: LoginController

  @BeforeTest
  fun before() {
    overrideSchedulers(main = { TestScheduler() }, io = { TestScheduler() })
    reaktiveUncaughtErrorHandler = { throw it }
  }

  @AfterTest
  fun after() {
    resetReaktiveUncaughtErrorHandler()
  }

  @Test
  fun shows_empty_login_WHEN_created() {
    createController()

    assertEquals("", loginView.model.login)
  }

  @Test
  fun shows_empty_password_WHEND_created() {
    createController()

    assertEquals("", loginView.model.password)
  }

  @Test
  fun shows_changed_login_WHEN_login_changed() {
    createController()

    val login = "test"
    loginView.dispatch(LoginView.Event.LoginChanged(login = login))

    val displayedData = loginView.model.login
    assertEquals(login, displayedData)
  }

  @Test
  fun shows_changed_password_WHEN_password_changed() {
    createController()

    val password = "test"
    loginView.dispatch(LoginView.Event.PasswordChanged(password = password))

    val displayedData = loginView.model.password
    assertEquals(password, displayedData)
  }

  @Test
  fun shows_Invalid_Credentials_Error_When_Invalid_Credentials(){
    createController()

    loginView.dispatch(LoginView.Event.LoginChanged(invalidLogin))
    loginView.dispatch(LoginView.Event.PasswordChanged(invalidPassword))
    loginView.dispatch(LoginView.Event.Auth)

    assertEquals(loginView.model.error, INVALID_AUTH_CREDENTIALS)
  }

  @Test
  fun publishes_Output_Auth_WHEN_Event_Auth() {
    createController()

    loginView.dispatch(LoginView.Event.LoginChanged(testLogin))
    loginView.dispatch(LoginView.Event.PasswordChanged(testPassword))
    loginView.dispatch(LoginView.Event.Auth)

    assertTrue(Output.Authorized in output)
  }

  @Test
  fun not_publishes_Output_Auth_WHEN_Event_Auth_With_Invalid_Credentials() {
    createController()

    loginView.dispatch(LoginView.Event.LoginChanged(invalidLogin))
    loginView.dispatch(LoginView.Event.PasswordChanged(invalidPassword))
    loginView.dispatch(LoginView.Event.Auth)

    assertTrue(output.isEmpty())
  }


  private fun createController() {
    controller = LoginControllerImpl(dependencies)
    controller.onViewCreated(loginView, lifecycle)
    lifecycle.resume()
  }
}
