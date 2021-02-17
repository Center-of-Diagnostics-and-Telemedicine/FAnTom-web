package controller

import TestResearchRepository
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import model.Filter
import repository.ResearchRepository
import resume
import testFilters
import testResearches
import view.*
import kotlin.test.*

class ListControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<ListController.Output>()

  private val dependencies =
    object : ListController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory
      override val lifecycle: Lifecycle = this@ListControllerTest.lifecycle
      override val researchRepository: ResearchRepository = TestResearchRepository()
      override val listOutput: (ListController.Output) -> Unit = { output += it }
    }

  private val listView = TestListView()
  private val filterView = TestFilterView()
  private val categoryView = TestCategoryView()
  private lateinit var controller: ListController

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
  fun shows_researches_WHEN_created() {
    createController()

    assertEquals(testResearches, listView.model.items)
  }

  @Test
  fun shows_filters_WHEN_created() {
    createController()

    assertEquals(testFilters, filterView.model.items)
  }

  @Test
  fun shows_all_filter_WHEN_All_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.All))

    assertEquals(Filter.All, filterView.model.current)
  }

  @Test
  fun shows_all_researches_WHEN_All_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.All))

    assertEquals(testResearches, listView.model.items)
  }

  @Test
  fun shows_NotSeen_filter_WHEN_NotSeen_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.NotSeen))

    assertEquals(Filter.NotSeen, filterView.model.current)
  }

  @Test
  fun shows_NotSeen_researches_WHEN_NotSeen_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.NotSeen))

    assertEquals(testResearches.filter { it.seen.not() }, listView.model.items)
  }

  @Test
  fun shows_Seen_filter_WHEN_Seen_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.Seen))

    assertEquals(Filter.Seen, filterView.model.current)
  }

  @Test
  fun shows_Seen_researches_WHEN_Seen_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.Seen))

    assertEquals(testResearches.filter { it.seen }, listView.model.items)
  }

  @Test
  fun shows_Done_filter_WHEN_Done_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.Done))

    assertEquals(Filter.Done, filterView.model.current)
  }

  @Test
  fun shows_Done_researches_WHEN_Done_Filter_applied() {
    createController()
    filterView.dispatch(FilterView.Event.ItemClick(Filter.Done))

    assertEquals(testResearches.filter { it.done }, listView.model.items)
  }

  @Test
  fun publishes_Output_ItemSelected_WHEN_Event_ItemClicked() {
    val testItem = testResearches.first()
    createController()

    listView.dispatch(ListView.Event.ItemClick(research = testItem))

    assertTrue(ListController.Output.ItemSelected(research = testItem) in output)
  }

  private fun createController() {
    controller = ListControllerImpl(dependencies)
    controller.onViewCreated(listView, filterView, categoryView, lifecycle)
    lifecycle.resume()
  }
}
