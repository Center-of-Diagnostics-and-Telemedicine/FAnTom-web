package research.dosereport

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mDivider
import components.alert
import controller.ExpertMarksController
import controller.ExpertMarksControllerImpl
import destroy
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import model.Research
import model.ResearchSlicesSizesDataNew
import react.*
import repository.ExpertMarksRepository
import repository.ExpertRoiRepository
import research.expert.ExpertMarksViewProxy
import research.expert.expertMarkItem
import resume
import root.debugLog
import styled.css
import styled.styledDiv
import view.ExpertMarksView
import view.initialExpertMarksModel

class DoseReportMarksComponent(prps: DoseReportMarksProps) :
  RComponent<DoseReportMarksProps, DoseReportMarksState>(prps) {

  private var expandedItem: String? = null

  private val marksViewDelegate = ExpertMarksViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ExpertMarksController

  init {
    state = DoseReportMarksState(initialExpertMarksModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(
      marksViewDelegate,
      lifecycleRegistry
    )
    val dependencies = props.dependencies
    val disposable = dependencies.expertMarksInput.subscribe { controller.input(it) }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
  }

  private fun createController(): ExpertMarksController {
    val dependencies = props.dependencies
    val marksControllerDependencies =
      object : ExpertMarksController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return ExpertMarksControllerImpl(marksControllerDependencies)
  }

  override fun RBuilder.render() {
    alert(
      message = state.model.error,
      open = state.model.error.isNotEmpty(),
      handleClose = { marksViewDelegate.dispatch(ExpertMarksView.Event.DismissError) }
    )
    mDivider()

    styledDiv {
      css {
        display = Display.flex
        flexDirection = FlexDirection.column
      }
      mList {
        state.model.rois.forEach { lungLobeModel ->
          debugLog(lungLobeModel.roiModel.toString())
          val panelName = "panel_${lungLobeModel.roiModel.id}"

          expertMarkItem(
            model = lungLobeModel,
            handlePanelClick = { expandClick(panelName) },
            handleVariantClick = {  },
            expand = expandedItem == panelName,
            fullWidth = props.dependencies.open
          )
        }
      }
    }
  }

  private fun expandClick(panelName: String) =
    setState { expandedItem = if (expandedItem == panelName) null else panelName }


  private fun updateState(marksModel: ExpertMarksView.Model) = setState { model = marksModel }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val expertMarksOutput: (ExpertMarksController.Output) -> Unit
    val data: ResearchSlicesSizesDataNew
    val expertMarksInput: Observable<ExpertMarksController.Input>
    val expertMarksRepository: ExpertMarksRepository
    val expertRoiRepository: ExpertRoiRepository
    val open: Boolean
    val research: Research
  }
}

class DoseReportMarksState(var model: ExpertMarksView.Model) : RState

interface DoseReportMarksProps : RProps {
  var dependencies: DoseReportMarksComponent.Dependencies
}

fun RBuilder.doseReportMarks(dependencies: DoseReportMarksComponent.Dependencies) =
  child(DoseReportMarksComponent::class) {
    attrs.dependencies = dependencies
  }