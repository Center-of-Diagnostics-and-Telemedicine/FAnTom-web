package research.expert

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import controller.ExpertMarksController
import controller.ExpertMarksControllerImpl
import destroy
import model.Research
import model.ResearchData
import react.*
import repository.ExpertMarksRepository
import resume
import view.ExpertMarksView
import view.initialExpertMarksModel

class ExpertMarksComponent(prps: ExpertMarksProps) :
  RComponent<ExpertMarksProps, ExpertMarksState>(prps) {

  private var expandedItem: String? = null

  private val marksViewDelegate = ExpertMarksViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: ExpertMarksController

  init {
    state = ExpertMarksState(initialExpertMarksModel())
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
//    alert(
//      message = state.model.error,
//      open = state.model.error.isNotEmpty(),
//      handleClose = { marksViewDelegate.dispatch(ExpertMarksView.Event.DismissError) }
//    )
//    mDivider()
//
//    styledDiv {
//      css {
//        display = Display.flex
//        flexDirection = FlexDirection.column
//      }
//      mList {
//        state.model..values.forEach { lungLobeModel ->
//          val panelName = "panel${lungLobeModel.shortName}"
//
//          expertMarkView(
//            model = lungLobeModel,
//            handlePanelClick = { expandClick(panelName) },
//            handleVariantClick = {
//              marksViewDelegate.dispatch(ExpertMarksView.Event.VariantChosen(lungLobeModel, it))
//            },
//            expand = expandedItem == panelName,
//            fullWidth = props.dependencies.open
//          )
//        }
//      }
//    }
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
    val data: ResearchData
    val expertMarksInput: Observable<ExpertMarksController.Input>
    val expertMarksRepository: ExpertMarksRepository
    val open: Boolean
    val research: Research
  }
}

class ExpertMarksState(var model: ExpertMarksView.Model) : RState

interface ExpertMarksProps : RProps {
  var dependencies: ExpertMarksComponent.Dependencies
}

fun RBuilder.expertMarks(dependencies: ExpertMarksComponent.Dependencies) =
  child(ExpertMarksComponent::class) {
    attrs.dependencies = dependencies
  }