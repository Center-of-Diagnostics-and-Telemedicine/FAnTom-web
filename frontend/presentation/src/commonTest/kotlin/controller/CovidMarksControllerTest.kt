package controller

import TestCovidMarksRepository
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import model.Research
import model.ResearchSlicesSizesDataNew
import model.toResearchSlicesSizesData
import repository.CovidMarksRepository
import resume
import testLungLobeModel
import testLungLobeValue
import testResearchInitModelCT
import testResearches
import view.CovidMarksView
import view.TestCovidMarksView
import kotlin.test.*

class CovidMarksControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<CovidMarksController.Output>()
  private val researchData = testResearchInitModelCT.toResearchSlicesSizesData()

  private val dependencies =
    object : CovidMarksController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory
      override val lifecycle: Lifecycle = this@CovidMarksControllerTest.lifecycle
      override val covidMarksRepository: CovidMarksRepository = TestCovidMarksRepository()
      override val covidMarksOutput: (CovidMarksController.Output) -> Unit = { output += it }
      override val research: Research = testResearches.first()
      override val data: ResearchSlicesSizesDataNew = researchData
    }

  private val covidMarksView = TestCovidMarksView()
  private lateinit var controller: CovidMarksController

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
  fun shows_CovidMark_WHEN_created() {
    createController()

    assertTrue { covidMarksView.model.lungLobeModels.isNotEmpty() }
  }

  @Test
  fun shows_updated_CovidMark_WHEN_choosing_variant() {
    createController()
    covidMarksView.dispatch(
      CovidMarksView.Event.VariantChosen(testLungLobeModel, testLungLobeValue.value)
    )

    assertEquals(
      covidMarksView.model.lungLobeModels[testLungLobeModel.id]?.value,
      testLungLobeValue
    )
  }

  /**
   * Not publishes because variants are not chosen
   */
  @Test
  fun not_publishes_CloseResearch_Output_WHEN_CloseResearchRequested_input_incoming() {
    createController()
    controller.input(CovidMarksController.Input.CloseResearchRequested)

    assertTrue(CovidMarksController.Output.CloseResearch in output)
  }

  /**
   * Publishes because variants are chosen
   */
  @Test
  fun publishes_CloseResearch_Output_WHEN_CloseResearchRequested_input_incoming() {
    createController()
    covidMarksView.model.lungLobeModels.values.forEach {
      covidMarksView.dispatch(
        CovidMarksView.Event.VariantChosen(it, it.availableValues.first().value)
      )
    }

    controller.input(CovidMarksController.Input.CloseResearchRequested)

    assertTrue(CovidMarksController.Output.CloseResearch in output)
  }

  private fun createController() {
    controller = CovidMarksControllerImpl(dependencies)
    controller.onViewCreated(covidMarksView, lifecycle)
    lifecycle.resume()
  }
}