package controller

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import model.Cut
import model.Research
import resume
import testCut
import testResearch
import view.SliderView
import view.TestSliderView
import kotlin.test.*

class SliderControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<SliderController.Output>()

  private val dependencies =
    object : SliderController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory
      override val lifecycle: Lifecycle = this@SliderControllerTest.lifecycle
      override val cut: Cut = testCut
      override val research: Research = testResearch
      override val sliderOutput: (SliderController.Output) -> Unit = { output += it }
    }

  private val sliderView = TestSliderView()
  private lateinit var controller: SliderController

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
  fun shows_Slider_Data_WHEN_created() {
    createController()

    assertNotNull(sliderView.model.currentValue)
  }

  @Test
  fun shows_middle_slice_number_of_range_WHEN_created() {
    createController()

    assertEquals(testCut.data.nImages / 2, sliderView.model.currentValue)
  }

  @Test
  fun shows_max_slice_number_WHEN_created() {
    createController()

    assertEquals(testCut.data.nImages, sliderView.model.maxValue)
  }

  @Test
  fun shows_changed_slice_number_WHEN_slider_changed() {
    createController()
    val newValue = sliderView.model.currentValue + 1
    sliderView.dispatch(SliderView.Event.HandleOnChange(newValue))

    assertEquals(newValue, sliderView.model.currentValue)
  }

  private fun createController() {
    controller = SliderControllerImpl(dependencies)
    controller.onViewCreated(sliderView, lifecycle)
    lifecycle.resume()
  }
}