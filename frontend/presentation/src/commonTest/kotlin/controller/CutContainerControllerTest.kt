package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import model.Grid
import model.ResearchSlicesSizesDataNew
import model.toResearchSlicesSizesData
import resume
import testCut
import testCutType
import testResearchInitModelCT
import view.TestCutContainerView
import kotlin.test.*

class CutContainerControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<CutsContainerController.Output>()
  private val researchData = testResearchInitModelCT.toResearchSlicesSizesData()

  private val dependencies =
    object : CutsContainerController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory
      override val lifecycle: Lifecycle = this@CutContainerControllerTest.lifecycle
      override val data: ResearchSlicesSizesDataNew = researchData
    }

  private val cutsContainerView = TestCutContainerView()
  private lateinit var controller: CutsContainerController

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
  fun shows_Cuts_WHEN_created() {
    createController()

    assertNotNull(cutsContainerView.model.items)
  }

  @Test
  fun shows_new_grid_WHEN_grid_changed() {
    createController()
    controller.input(CutsContainerController.Input.ChangeGrid(Grid.Single(testCutType)))

    assertTrue { cutsContainerView.model.grid is Grid.Single }
  }

  @Test
  fun shows_axial_grid_type_WHEN_grid_changed_to_single() {
    createController()
    controller.input(CutsContainerController.Input.ChangeGrid(Grid.Single(testCutType)))

    assertEquals(cutsContainerView.model.grid.types.first(), testCutType)
  }

  @Test
  fun shows_changed_cutType_WHEN_input_changeCutType_incoming() {
    createController()
    controller.input(CutsContainerController.Input.ChangeCutType(testCutType, testCut))


  }

  private fun createController() {
    controller = CutsContainerControllerImpl(dependencies)
    controller.onViewCreated(cutsContainerView, lifecycle)
    lifecycle.resume()
  }
}