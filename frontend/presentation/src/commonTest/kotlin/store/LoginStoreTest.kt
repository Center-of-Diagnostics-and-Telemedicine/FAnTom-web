package store

import TestLoginRepository
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

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

    private fun createStore() {
        store = LoginStoreFactory(DefaultStoreFactory, repository).create()
    }
}
