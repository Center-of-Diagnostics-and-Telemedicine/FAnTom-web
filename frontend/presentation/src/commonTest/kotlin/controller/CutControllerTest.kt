package controller

import repository.TestBrightnessRepository
import repository.TestMipRepository
import repository.TestResearchRepository
import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import controller.CutController.*
import model.*
import repository.BrightnessRepository
import repository.MipRepository
import repository.ResearchRepository
import resume
import testCircle
import testCut
import testImage
import testMark
import testMouseWheelPosition
import testPosition
import testResearch
import testSliceNumber
import view.*
import kotlin.test.*

class CutControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<Output>()

  private val dependencies =
    object : Dependencies {
      override val mipRepository: MipRepository = TestMipRepository()
      override val brightnessRepository: BrightnessRepository = TestBrightnessRepository()
      override val storeFactory: StoreFactory = DefaultStoreFactory
      override val lifecycle: Lifecycle = this@CutControllerTest.lifecycle
      override val researchRepository: ResearchRepository = TestResearchRepository()
      override val cutOutput: (Output) -> Unit = { output += it }
      override val cut: Plane = testCut
      override val research: Research = testResearch

    }

  private val cutView = TestCutView()
  private val shapesView = TestShapesView()
  private val drawView = TestDrawView()
  private lateinit var controller: CutController

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
  fun shows_Cut_WHEN_created() {
    createController()

    assertNotNull(cutView.model.slice)
  }

  @Test
  fun shows_slice_WHEN_loaded() {
    createController()

    assertEquals(cutView.model.slice, testImage)
  }

  @Test
  fun shows_has_slice_number_WHEN_loaded() {
    createController()

    assertEquals(cutView.model.sliceNumber, testSliceNumber)
  }

  @Test
  fun shows_slice_number_WHEN_loaded() {
    createController()

    assertEquals(shapesView.model.sliceNumber, testSliceNumber)
  }

  @Test
  fun shows_circle_WHEN_drawing() {
    createController()
    drawView.dispatch(
      DrawView.Event.MouseDown(
        x = testPosition,
        y = testPosition,
        metaKey = true,
        button = LEFT_MOUSE_BUTTON,
        shiftKey = false,
        altKey = false
      )
    )
    drawView.dispatch(DrawView.Event.MouseMove(testPosition * 2, testPosition * 2))

    assertNotNull(drawView.model.shape)
  }

  @Test
  fun shows_rectangle_WHEN_drawing() {
    createController()
    drawView.dispatch(
      DrawView.Event.MouseDown(
        x = testPosition,
        y = testPosition,
        metaKey = false,
        button = LEFT_MOUSE_BUTTON,
        shiftKey = true,
        altKey = false
      )
    )
    drawView.dispatch(DrawView.Event.MouseMove(testPosition * 2, testPosition * 2))

    assertNotNull(drawView.model.shape)
  }

  @Test
  fun shows_no_shape_WHEN_stop_drawing_circle() {
    createController()
    drawCircle()

    assertNull(drawView.model.shape)
  }

  @Test
  fun shows_no_shape_WHEN_stop_drawing_rectangle() {
    createController()
    drawRectangle()

    assertNull(drawView.model.shape)
  }

  @Test
  fun shows_no_shape_WHEN_not_drawing() {
    createController()
    moveMouseContrastBrightness()

    assertNull(drawView.model.shape)
  }

  @Test
  fun shows_changed_slice_number_WHEN_mousewheel_forced() {
    createController()
    drawView.dispatch(DrawView.Event.MouseWheel(testPosition.toInt()))

    assertNotEquals(shapesView.model.sliceNumber, testSliceNumber)
  }

  @Test
  fun shows_shapes_WHEN_marks_incoming() {
    createController()
    controller.input(Input.Marks(listOf(testMark)))

    assertNotNull(shapesView.model.shapes)
  }

  @Test
  fun shows_movable_rects_for_shapes_WHEN_marks_incoming() {
    createController()
    controller.input(Input.Marks(listOf(testMark)))

    assertNotNull(shapesView.model.rects)
  }

  @Test
  fun publishes_SliceNumberChanged_Output_WHEN_event_mouseWheel() {
    createController()
    drawView.dispatch(DrawView.Event.MouseWheel(testMouseWheelPosition))

    val newSLiceNumber = testSliceNumber + testMouseWheelPosition
    assertTrue(Output.SliceNumberChanged(newSLiceNumber, testCut) in output)
  }

  @Test
  fun publishes_CircleDrawn_Output_WHEN_stop_drawing() {
    createController()
    drawCircle()

    assertTrue(Output.CircleDrawn(testCircle, testSliceNumber, testCut) in output)
  }

  @Test
  fun publishes_ContrastBrightnessChanged_Output_WHEN_changing_contrast_or_brightness() {
    createController()
    moveMouseContrastBrightness()
    drawView.dispatch(DrawView.Event.MouseUp)

    println(output.first())

    assertTrue(
      Output.ContrastBrightnessChanged(
        black = (INITIAL_BLACK - testPosition * 2).toInt(),
        white = (INITIAL_WHITE).toInt()
      ) in output
    )
  }

  @Test
  fun publishes_OpenFullCut_Output_WHEN_double_click() {
    createController()
    drawView.dispatch(DrawView.Event.DoubleClick)

    assertTrue(Output.OpenFullCut(testCut) in output)
  }

  @Test
  fun publishes_ChangeCutType_Output_WHEN_changing_cut_type() {
    createController()
    shapesView.dispatch(ShapesView.Event.CutTypeOnChange(CutType.CT_FRONTAL))

    assertTrue(Output.ChangeCutType(CutType.CT_FRONTAL, testCut ) in output)
  }


//  RectangleDrawn

  private fun moveMouseContrastBrightness() {
    drawView.dispatch(
      DrawView.Event.MouseDown(
        x = testPosition,
        y = testPosition,
        metaKey = false,
        button = MIDDLE_MOUSE_BUTTON,
        shiftKey = false,
        altKey = false
      )
    )
    drawView.dispatch(DrawView.Event.MouseMove(testPosition * 2, testPosition * 2))
  }

  private fun drawCircle() {
    drawView.dispatch(
      DrawView.Event.MouseDown(
        x = testPosition,
        y = testPosition,
        metaKey = true,
        button = LEFT_MOUSE_BUTTON,
        shiftKey = false,
        altKey = false
      )
    )
    drawView.dispatch(DrawView.Event.MouseMove(x = testPosition * 2, y = testPosition * 2))
    drawView.dispatch(DrawView.Event.MouseUp)
  }

  private fun drawRectangle() {
    drawView.dispatch(
      DrawView.Event.MouseDown(
        x = testPosition,
        y = testPosition,
        metaKey = false,
        button = LEFT_MOUSE_BUTTON,
        shiftKey = true,
        altKey = false
      )
    )
    drawView.dispatch(DrawView.Event.MouseMove(x = testPosition * 2, y = testPosition * 2))
    drawView.dispatch(DrawView.Event.MouseUp)
  }

  private fun createController() {
    controller = CutControllerImpl(dependencies)
    controller.onViewCreated(cutView, shapesView, drawView, lifecycle)
    lifecycle.resume()
  }
}