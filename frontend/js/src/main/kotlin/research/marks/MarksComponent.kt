package research.marks

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.mDivider
import com.ccfraser.muirwik.components.mPaper
import com.ccfraser.muirwik.components.table.mTable
import com.ccfraser.muirwik.components.table.mTableBody
import com.ccfraser.muirwik.components.table.mTableHead
import com.ccfraser.muirwik.components.table.mTableRow
import com.ccfraser.muirwik.components.themeContext
import components.alert
import controller.MarksController
import controller.MarksControllerImpl
import destroy
import model.Research
import model.ResearchData
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
    val disposable = dependencies.marksInput.subscribe { controller.input(it) }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
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
        handleClose = { marksViewDelegate.dispatch(MarksView.Event.DismissError) }
      )
      mDivider()

      mPaper {
        mTable {
          mTableHead {
            mTableRow {
//              mTableCell(align = MTableCellAlign.center) {
//                css { padding = "8px" }
//                mIcon("visibility")
//              }
              tableCell("X")
              tableCell("Y")

              if (props.dependencies.isPlanar) {
                tableCell("mm_v")
                tableCell("mm_h")
              } else {
                tableCell("Z")
                tableCell("mm")
              }
              tableCell("Тип")
              tableCell("")
            }
          }
          mTableBody {
            state.model.items.forEach { area ->
              markView(
                mark = area,
                eventOutput = { marksViewDelegate.dispatch(it) },
                markTypes = state.model.markTypes,
                isPlanar = props.dependencies.isPlanar
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
    val research: Research
    val data: ResearchData
    val isPlanar: Boolean
  }
}

class MarksState(var model: Model) : RState

interface MarksProps : RProps {
  var dependencies: MarksComponent.Dependencies
}

fun RBuilder.marks(dependencies: MarksComponent.Dependencies) = child(MarksComponent::class) {
  attrs.dependencies = dependencies
}
