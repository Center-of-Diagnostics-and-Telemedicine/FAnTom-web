package store

import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import model.toResearchSlicesSizesData
import store.tools.ToolsStore
import testResearchInitModelCT
import kotlin.test.*

class ToolsStoreTest {

  private val testData = testResearchInitModelCT.toResearchSlicesSizesData(doseReport)

  private lateinit var store: ToolsStore

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
  fun has_list_of_tools_WHEN_created() {
    createStore()

    assertTrue(store.state.list.isNotEmpty())
  }

  private fun createStore() {
    store = ToolsStoreFactory(DefaultStoreFactory, testData).create()
  }
}