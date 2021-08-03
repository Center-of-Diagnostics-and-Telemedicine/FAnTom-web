package controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.badoo.reaktive.scheduler.overrideSchedulers
import com.badoo.reaktive.test.scheduler.TestScheduler
import com.badoo.reaktive.utils.reaktiveUncaughtErrorHandler
import com.badoo.reaktive.utils.resetReaktiveUncaughtErrorHandler
import model.*
import repository.BrightnessRepository
import repository.MipRepository
import repository.TestBrightnessRepository
import repository.TestMipRepository
import resume
import testMips
import testPresets
import testResearchInitModelCT
import testTools
import view.*
import kotlin.test.*

class ToolsControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<ToolsController.Output>()
  private val researchData = testResearchInitModelCT.toResearchSlicesSizesData(false)

  private val dependencies =
    object : ToolsController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory()
      override val lifecycle: Lifecycle = this@ToolsControllerTest.lifecycle
      override val toolsOutput: (ToolsController.Output) -> Unit = { output += it }
      override val data: ResearchData = researchData
      override val mipRepository: MipRepository = TestMipRepository()
      override val brightnessRepository: BrightnessRepository = TestBrightnessRepository()
    }

  private val toolsView = TestToolsView()
  private val gridView = TestGridView()
  private val mipView = TestMipView()
  private val brightnessView = TestBrightnessView()
  private val presetView = TestPresetView()
  private lateinit var controller: ToolsController

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
  fun shows_tools_WHEN_created() {
    createController()

    assertEquals(testTools, toolsView.model.items)
  }

  @Test
  fun shows_MipTool_WHEN_MipTool_Clicked() {
    createController()
    toolsView.dispatch(ToolsView.Event.ItemClick(Tool.MIP))

    assertEquals(Tool.MIP, toolsView.model.current)
  }

  @Test
  fun shows_BrightnessTool_WHEN_BrightnessTool_Clicked() {
    createController()
    toolsView.dispatch(ToolsView.Event.ItemClick(Tool.Brightness))

    assertEquals(Tool.Brightness, toolsView.model.current)
  }

  @Test
  fun shows_PresetTool_WHEN_PresetTool_Clicked() {
    createController()
    toolsView.dispatch(ToolsView.Event.ItemClick(Tool.Preset))

    assertEquals(Tool.Preset, toolsView.model.current)
  }

//  @Test
//  fun shows_Grids_WHEN_created() {
//    createController()
//
//    assertEquals(testGrids, gridView.model.items)
//  }
//
//  @Test
//  fun shows_FourGrid_WHEN_created() {
//    createController()
//
//    assertEquals(initialFourGrid(researchData.type), gridView.model.current)
//  }
//
//  @Test
//  fun shows_SingleGrid_WHEN_SingleGrid_Clicked() {
//    createController()
//    gridView.dispatch(GridView.Event.ItemClick(GridType.Single))
//
//    assertEquals(initialSingleGrid(researchData.type), gridView.model.current)
//  }
//
//  @Test
//  fun shows_TwoVerticalGrid_WHEN_TwoVerticalGrid_Clicked() {
//    createController()
//    gridView.dispatch(GridView.Event.ItemClick(GridType.TwoVertical))
//
//    assertEquals(initialTwoVerticalGrid(researchData.type), gridView.model.current)
//  }
//
//  @Test
//  fun shows_TwoHorizontalGrid_WHEN_TwoHorizontalGrid_Clicked() {
//    createController()
//    gridView.dispatch(GridView.Event.ItemClick(GridType.TwoHorizontal))
//
//    assertEquals(initialTwoHorizontalGrid(researchData.type), gridView.model.current)
//  }
//
//  @Test
//  fun shows_FourGrid_WHEN_FourGrid_Clicked() {
//    createController()
//    gridView.dispatch(GridView.Event.ItemClick(GridType.Four))
//
//    assertEquals(initialFourGrid(researchData.type), gridView.model.current)
//  }

  @Test
  fun shows_Mips_WHEN_created() {
    createController()

    assertEquals(testMips, mipView.model.items)
  }

  @Test
  fun shows_NoMip_WHEN_created() {
    createController()

    assertEquals(Mip.No, mipView.model.current)
  }

  @Test
  fun shows_NoMip_WHEN_NoMip_Clicked() {
    createController()
    mipView.dispatch(MipView.Event.ItemClick(Mip.No))

    assertEquals(Mip.No, mipView.model.current)
  }

  @Test
  fun shows_AverageMip_WHEN_AverageMip_Clicked() {
    createController()
    mipView.dispatch(MipView.Event.ItemClick(Mip.Average()))

    assertEquals(Mip.Average(), mipView.model.current)
  }

  @Test
  fun shows_MaxMip_WHEN_MaxMip_Clicked() {
    createController()
    mipView.dispatch(MipView.Event.ItemClick(Mip.Max()))

    assertEquals(Mip.Max(), mipView.model.current)
  }

  @Test
  fun shows_MipValue_WHEN_MipValueChanged() {
    val testValue = 10
    createController()

    mipView.dispatch(MipView.Event.MipValueChanged(testValue))

    assertEquals(testValue, mipView.model.currentValue)
  }

  @Test
  fun shows_Presets_WHEN_created() {
    createController()

    assertEquals(testPresets, presetView.model.items)
  }

  @Test
  fun shows_SoftTissue_WHEN_created() {
    createController()

    assertEquals(Presets.SoftTissue, presetView.model.current)
  }

  @Test
  fun shows_SoftTissuePreset_WHEN_SoftTissuePreset_clicked() {
    createController()

    presetView.dispatch(PresetView.Event.ItemClick(Presets.SoftTissue))

    assertEquals(Presets.SoftTissue, presetView.model.current)
  }

  @Test
  fun shows_Bones_WHEN_Bones_clicked() {
    createController()

    presetView.dispatch(PresetView.Event.ItemClick(Presets.Bones))

    assertEquals(Presets.Bones, presetView.model.current)
  }

  @Test
  fun shows_Lung_WHEN_Lung_clicked() {
    createController()

    presetView.dispatch(PresetView.Event.ItemClick(Presets.Lungs))

    assertEquals(Presets.Lungs, presetView.model.current)
  }

  @Test
  fun shows_Vessels_WHEN_Vessels_clicked() {
    createController()

    presetView.dispatch(PresetView.Event.ItemClick(Presets.Vessels))

    assertEquals(Presets.Vessels, presetView.model.current)
  }

  @Test
  fun shows_Brain_WHEN_Brain_clicked() {
    createController()

    presetView.dispatch(PresetView.Event.ItemClick(Presets.Brain))

    assertEquals(Presets.Brain, presetView.model.current)
  }

  @Test
  fun publishes_Output_Auth_WHEN_Event_Auth() {
    createController()

    toolsView.dispatch(ToolsView.Event.BackClick)

    assertTrue(ToolsController.Output.Back in output)
  }

  private fun createController() {
    controller = ToolsControllerImpl(dependencies)
    controller.onViewCreated(toolsView, gridView, mipView, brightnessView, presetView, lifecycle)
    lifecycle.resume()
  }
}
