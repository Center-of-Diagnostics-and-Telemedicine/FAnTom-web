package research.marks

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.table.*
import com.ccfraser.muirwik.components.themeContext
import components.alert
import controller.MarksController
import controller.MarksControllerImpl
import destroy
import model.ResearchSlicesSizesDataNew
import react.*
import repository.MarksRepository
import resume
import view.MarksView
import view.MarksView.Model
import view.initialMarksModel

class MarksComponent(prps: MarksProps) : RComponent<MarksProps, MarksState>(prps) {

  private val marksViewDelegate = MarksViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: MarksController
  private val disposable = CompositeDisposable()

  init {
    state = MarksState(initialMarksModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(
      marksViewDelegate,
      lifecycleRegistry
    )
    val dependencies = props.dependencies
    disposable.add(dependencies.marksInput.subscribe { controller.input(it) })
  }

  private fun createController(): MarksController {
    val dependencies = props.dependencies
    val marksControllerDependencies =
      object : MarksController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return MarksControllerImpl(marksControllerDependencies)
  }

  override fun RBuilder.render() {
    themeContext.Consumer { theme ->

      alert(
        message = state.model.error,
        open = state.model.error.isNotEmpty(),
        handleClose = { marksViewDelegate.dispatch(MarksView.Event.DissmissError) }
      )

      mPaper {
        mTable {
          mTableHead {
            mTableRow {
              mTableCell(align = MTableCellAlign.center) { +"X" }
              mTableCell(align = MTableCellAlign.center) { +"Y" }
              if (props.dependencies.isPlanar.not()) {
                mTableCell(align = MTableCellAlign.center) { +"Z" }
              }
              mTableCell(align = MTableCellAlign.center) { +"mm_v" }
              mTableCell(align = MTableCellAlign.center) { +"mm_h" }
              mTableCell(align = MTableCellAlign.center) { +"Тип" }
              mTableCell(align = MTableCellAlign.center) { }
            }
          }
          mTableBody {
            state.model.items.forEach { area ->
              markView(
                mark = area,
                eventOutput = { marksViewDelegate.dispatch(it) },
                markTypes = state.model.markTypes
              )
            }
          }
        }
      }
    }
  }

  fun updateState(marksModel: Model) = setState { model = marksModel }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val marksOutput: (MarksController.Output) -> Unit
    val marksInput: Observable<MarksController.Input>
    val marksRepository: MarksRepository
    val researchId: Int
    val isPlanar: Boolean
    val data: ResearchSlicesSizesDataNew
  }
}

class MarksState(var model: Model) : RState

interface MarksProps : RProps {
  var dependencies: MarksComponent.Dependencies
}

fun RBuilder.marks(dependencies: MarksComponent.Dependencies) = child(MarksComponent::class) {
  attrs.dependencies = dependencies
}
