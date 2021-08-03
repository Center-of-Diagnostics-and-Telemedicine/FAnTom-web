package research.covid

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.mDivider
import components.alert
import controller.CovidMarksController
import controller.CovidMarksControllerImpl
import destroy
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import model.Research
import model.ResearchData
import react.*
import repository.CovidMarksRepository
import resume
import styled.css
import styled.styledDiv
import view.CovidMarksView
import view.CovidMarksView.Model
import view.initialCovidMarksModel

class CovidMarksComponent(prps: CovidMarksProps) :
  RComponent<CovidMarksProps, CovidMarksState>(prps) {

  private var expandedItem: String? = null

  private val marksViewDelegate = CovidMarksViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: CovidMarksController

  init {
    state = CovidMarksState(initialCovidMarksModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(
      marksViewDelegate,
      lifecycleRegistry
    )
    val dependencies = props.dependencies
    val disposable = dependencies.covidMarksInput.subscribe { controller.input(it) }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
  }

  private fun createController(): CovidMarksController {
    val dependencies = props.dependencies
    val marksControllerDependencies =
      object : CovidMarksController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return CovidMarksControllerImpl(marksControllerDependencies)
  }

  override fun RBuilder.render() {
    alert(
      message = state.model.error,
      open = state.model.error.isNotEmpty(),
      handleClose = { marksViewDelegate.dispatch(CovidMarksView.Event.DismissError) }
    )
    mDivider()

    styledDiv {
      css {
        display = Display.flex
        flexDirection = FlexDirection.column
      }
      mList {
        state.model.lungLobeModels.values.forEach { lungLobeModel ->
          val panelName = "panel${lungLobeModel.shortName}"

          covidMarkView(
            model = lungLobeModel,
            handlePanelClick = { expandClick(panelName) },
            handleVariantClick = {
              marksViewDelegate.dispatch(CovidMarksView.Event.VariantChosen(lungLobeModel, it))
            },
            expand = expandedItem == panelName,
            fullWidth = props.dependencies.open
          )
        }
      }
    }
  }

  private fun expandClick(panelName: String) =
    setState { expandedItem = if (expandedItem == panelName) null else panelName }


  private fun updateState(marksModel: Model) = setState { model = marksModel }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val covidMarksRepository: CovidMarksRepository
    val covidMarksOutput: (CovidMarksController.Output) -> Unit
    val data: ResearchData
    val covidMarksInput: Observable<CovidMarksController.Input>
    val open: Boolean
    val research: Research
  }
}

class CovidMarksState(var model: Model) : RState

interface CovidMarksProps : RProps {
  var dependencies: CovidMarksComponent.Dependencies
}

fun RBuilder.covidMarks(dependencies: CovidMarksComponent.Dependencies) =
  child(CovidMarksComponent::class) {
    attrs.dependencies = dependencies
  }