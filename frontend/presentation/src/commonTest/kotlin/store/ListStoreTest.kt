package store

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import repository.TestResearchRepository
import store.list.ListStore
import testCategory
import testFilters
import kotlin.test.*

class ListStoreTest {

  private val repository = TestResearchRepository()

  private lateinit var store: ListStore

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
  fun has_list_of_researches_WHEN_created() {
    createStore()

    assertTrue(store.state.list.isNotEmpty())
  }

  @Test
  fun shows_filtered_researches_WHEN_applying_filter() {
    createStore()
    store.accept(ListStore.Intent.HandleFilterChanged(testFilters.first(), testCategory))

    assertEquals(testCategory.name, store.state.list.first().category)
  }

  private fun createStore() {
    store = ListStoreFactory(DefaultStoreFactory(), repository).create()
  }
}