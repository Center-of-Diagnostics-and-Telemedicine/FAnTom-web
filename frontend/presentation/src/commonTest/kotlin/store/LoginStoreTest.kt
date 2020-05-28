package store

import TestLoginRepository
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import invalidLogin
import invalidPassword
import testLogin
import testPassword
import kotlin.test.*

class TodoListStoreTest {

  private val repository = TestLoginRepository()

  private lateinit var store: LoginStore

  @BeforeTest
  fun before() {
    overrideSchedulers(main = { TestScheduler() }, io = { TestScheduler() })
    reaktiveUncaughtErrorHandler = { throw it }
  }

  @AfterTest
  fun after() {
    overrideSchedulers()
    resetReaktiveUncaughtErrorHandler()
  }

  @Test
  fun has_empty_login_WHEN_created() {
    createStore()

    assertEquals("", store.state.login)
  }

  @Test
  fun has_empty_password_WHEN_created() {
    createStore()

    assertEquals("", store.state.password)
  }

  @Test
  fun has_no_error_WHEN_Auth() {
    createStore()
    store.accept(LoginStore.Intent.HandleLoginChanged(testLogin))
    store.accept(LoginStore.Intent.HandlePasswordChanged(testPassword))
    store.accept(LoginStore.Intent.Auth)

    assertEquals("", store.state.error)
  }

  @Test
  fun has_error_WHEN_Auth_with_invalid_credentials() {
    createStore()
    store.accept(LoginStore.Intent.HandleLoginChanged(invalidLogin))
    store.accept(LoginStore.Intent.HandlePasswordChanged(invalidPassword))
    store.accept(LoginStore.Intent.Auth)

    assertNotEquals("", store.state.error)
  }

  private fun createStore() {
    store = LoginStoreFactory(DefaultStoreFactory, repository).create()
  }
}
