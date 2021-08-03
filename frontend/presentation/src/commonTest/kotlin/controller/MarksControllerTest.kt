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
import model.ResearchData
import model.toResearchSlicesSizesData
import repository.MarksRepository
import repository.TestMarksRepository
import resume
import testComment
import testMark
import testMarkType
import testResearchInitModelCT
import testResearches
import view.MarksView
import view.TestMarksView
import kotlin.test.*

class MarksControllerTest {

  private val lifecycle = LifecycleRegistry()
  private val output = ArrayList<MarksController.Output>()
  private val researchData = testResearchInitModelCT.toResearchSlicesSizesData(false)

  private val dependencies =
    object : MarksController.Dependencies {
      override val storeFactory: StoreFactory = DefaultStoreFactory()
      override val lifecycle: Lifecycle = this@MarksControllerTest.lifecycle
      override val marksRepository: MarksRepository = TestMarksRepository()
      override val marksOutput: (MarksController.Output) -> Unit = { output += it }
      override val research: Research = testResearches.first()
      override val data: ResearchData = researchData
    }

  private val marksView = TestMarksView()
  private lateinit var controller: MarksController

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
  fun shows_Marks_WHEN_created() {
    createController()

    assertTrue { marksView.model.items.isNotEmpty() }
  }

  @Test
  fun shows_The_Selected_Mark_WHEN_selecting_a_Mark() {
    createController()
    marksView.dispatch(MarksView.Event.SelectItem(testMark))

    val actual = currentMark()
    assertNotNull(actual)
    assertEquals(testMark, actual)
  }

  @Test
  fun shows_Comment_WHEN_changing_comment() {
    createController()
    marksView.dispatch(MarksView.Event.ItemCommentChanged(testMark, testComment))

    val actual = currentMark()
    assertNotNull(actual)
    assertEquals(testComment, actual.comment)
  }

  @Test
  fun shows_Marks_Without_Deleted_Mark_WHEN_Deleting_Mark() {
    createController()
    marksView.dispatch(MarksView.Event.DeleteItem(testMark))

    assertTrue { testMark !in marksView.model.items }
  }

  @Test
  fun shows_New_Mark_Type_WHEN_Changing_Mark_Type() {
    createController()
    marksView.dispatch(MarksView.Event.ChangeMarkType(testMarkType, testMark.id))

    val current = currentMark()
    assertNotNull(current)
    assertEquals(testMarkType, current.type)
  }

  @Test
  fun hides_The_Mark_WHEN_Hide_Mark_Clicked() {
    createController()
    marksView.dispatch(MarksView.Event.ChangeVisibility(testMark))

    val current = currentMark()
    assertNotNull(current)
    assertNotEquals(testMark.visible, current.visible)
  }

  private fun currentMark() = marksView.model.items.firstOrNull { it.id == testMark.id }

  private fun createController() {
    controller = MarksControllerImpl(dependencies)
    controller.onViewCreated(marksView, lifecycle)
    lifecycle.resume()
  }
}