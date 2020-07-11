package research.marks

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.list.mList
import controller.MarksController
import controller.MarksControllerImpl
import destroy
import kotlinx.css.*
import react.*
import repository.MarksRepository
import resume
import styled.css
import styled.styledDiv
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
    styledDiv {
      css {
        height = 100.pct
        display = Display.flex
        flexDirection = FlexDirection.column
        justifyContent = JustifyContent.spaceBetween
      }

      mList {
        state.model.items.forEach { mark ->
          markView(mark, eventOutput = { marksViewDelegate.dispatch(it) })
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
  }
}

class MarksState(var model: Model) : RState

interface MarksProps : RProps {
  var dependencies: MarksComponent.Dependencies
}

fun RBuilder.marks(dependencies: MarksComponent.Dependencies) = child(MarksComponent::class) {
  attrs.dependencies = dependencies
}
