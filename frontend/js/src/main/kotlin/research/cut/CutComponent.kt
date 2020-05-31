package research.cut

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.lifecycle.doOnDestroy
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.ccfraser.muirwik.components.spacingUnits
import controller.CutController
import controller.CutControllerImpl
import destroy
import kotlinx.css.*
import kotlinx.css.properties.scaleY
import model.Cut
import react.*
import repository.ResearchRepository
import resume
import styled.css
import styled.styledImg
import view.CutView
import view.initialCutModel

class CutComponent(prps: CutProps) : RComponent<CutProps, CutState>(prps) {

  private val cutViewDelegate = CutViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private lateinit var controller: CutController

  init {
    state = CutState(initialCutModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    val dependencies = props.dependencies
    val disposable = dependencies.cutsInput.subscribe { controller.input(it) }
    lifecycleRegistry.doOnDestroy(disposable::dispose)
    controller.onViewCreated(cutViewDelegate, lifecycleRegistry)
  }

  private fun createController(): CutController {
    val dependencies = props.dependencies
    val cutControllerDependencies =
      object : CutController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return CutControllerImpl(cutControllerDependencies)
  }

  override fun RBuilder.render() {
    if (state.cutModel.slice.isNotEmpty()) {
      styledImg {
        css {
          transform.scaleY(-1)
          1.spacingUnits
          width = props.dependencies.width.px
          height = props.dependencies.height.px
          objectFit = ObjectFit.contain
        }
        attrs.src = "data:image/bmp;base64,${state.cutModel.slice}"
      }
    }
  }

  private fun updateState(model: CutView.Model) {
    setState { cutModel = model }
  }

  override fun componentWillUnmount() {
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val cut: Cut
    val cutsInput: Observable<CutController.Input>
    val cutOutput: (CutController.Output) -> Unit
    val researchRepository: ResearchRepository
    val researchId: Int
    val height: Int
    val width: Int
  }

}

class CutState(
  var cutModel: CutView.Model
) : RState

interface CutProps : RProps {
  var dependencies: CutComponent.Dependencies
}

fun RBuilder.cutView(
  dependencies: CutComponent.Dependencies
) = child(CutComponent::class) {
  attrs.dependencies = dependencies
}
