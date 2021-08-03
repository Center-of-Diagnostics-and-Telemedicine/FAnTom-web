package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import model.Research
import repository.ResearchRepository
import repository.TestResearchRepository
import resume
import testResearch
import view.TestResearchView
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class ResearchControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<ResearchController.Output>()

  private val dependencies =
    object : ResearchController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory()
      override val lifecycle: Lifecycle = this@ResearchControllerTest.lifecycle
      override val researchRepository: ResearchRepository = TestResearchRepository()
      override val research: Research = testResearch
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