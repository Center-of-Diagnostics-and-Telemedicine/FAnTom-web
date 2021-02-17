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
import repository.ResearchRepository
import resume
import testResearch
import view.TestResearchView
import kotlin.test.*

class ResearchControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<ResearchController.Output>()

  private val dependencies =
    object : ResearchController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory
      override val lifecycle: Lifecycle = this@ResearchControllerTest.lifecycle
      override val researchRepository: ResearchRepository = TestResearchRepository()
      override val researchId: Int = testResearch.id
      override val researchOutput: (ResearchController.Output) -> Unit = { output += it }
    }

  private val researchView = TestResearchView()
  private lateinit var controller: ResearchController

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
  fun shows_Research_Data_WHEN_created() {
    createController()

    assertNotNull(researchView.model.data)
  }

  private fun createController() {
    controller = ResearchControllerImpl(dependencies)
    controller.onViewCreated(researchView, lifecycle)
    lifecycle.resume()
  }
}