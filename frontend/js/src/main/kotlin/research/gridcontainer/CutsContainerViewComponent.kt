package research.gridcontainer

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.publish.PublishSubject
import controller.CutController
import controller.CutsContainerController
import controller.CutsContainerControllerImpl
import destroy
import kotlinx.css.*
import model.Cut
import model.Grid
import model.ResearchSlicesSizesData
import react.*
import repository.ResearchRepository
import research.cut.CutContainer
import research.cut.cutContainer
import research.gridcontainer.CutsContainerViewComponent.GridContainerStyles.columnOfRowsStyle
import research.gridcontainer.CutsContainerViewComponent.GridContainerStyles.rowOfColumnsStyle
import resume
import root.debugLog
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.CutsContainerView.Model
import view.initialCutsContainerModel

class CutsContainerViewComponent(prps: CutsContainerProps) :
  RComponent<CutsContainerProps, CutsContainerState>(prps) {

  private val gridViewDelegate = CutsContainerViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private val cutsInputObservable = PublishSubject<CutController.Input>()
  private lateinit var controller: CutsContainerController
  private val disposable = CompositeDisposable()

  init {
    state = CutsContainerState(initialCutsContainerModel())
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    val dependencies = props.dependencies
    disposable.add(dependencies.cutsContainerInputs.subscribe { controller.input(it) })
    disposable.add(dependencies.cutsInput.subscribe { cutsInputObservable.onNext(it) })
    controller.onViewCreated(gridViewDelegate, lifecycleRegistry)
  }

  private fun createController(): CutsContainerController {
    val dependencies = props.dependencies
    val researchControllerDependencies =
      object : CutsContainerController.Dependencies, Dependencies by dependencies {
        override val lifecycle: Lifecycle = lifecycleRegistry
      }
    return CutsContainerControllerImpl(researchControllerDependencies)
  }

  override fun RBuilder.render() {
    val cutsModel = state.cutsModel
    if (cutsModel.items.isNotEmpty()) {
      styledDiv {
        css(columnOfRowsStyle)
        when (cutsModel.grid) {
          is Grid.Single -> singleCutContainer(cutsModel.items.first())
          is Grid.TwoVertical -> twoVerticalCutsContainer(cutsModel.items)
          is Grid.TwoHorizontal -> twoHorizontalCutsContainer(cutsModel.items)
          is Grid.Four -> fourCutsContainer(cutsModel.items)
        }
      }
    }
  }

  private fun RBuilder.singleCutContainer(item: Cut) {
    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(item))
    }
  }

  private fun RBuilder.twoHorizontalCutsContainer(items: List<Cut>) {
    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(items.first()))
      cutContainer(dependencies = dependencies(items.last()))
    }
  }

  private fun RBuilder.twoVerticalCutsContainer(items: List<Cut>) {
    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(items.first()))
    }

    styledDiv {
      css(rowOfColumnsStyle)
      cutContainer(dependencies = dependencies(items.last()))
    }
  }

  private fun RBuilder.fourCutsContainer(items: List<Cut>) {
    val leftTop = items[0]
    val rightTop = items[1]
    val leftBottom = items[2]
    val rightBottom = items[3]

    //это колонка строк срезов(верх низ)
    styledDiv {
      css(columnOfRowsStyle)

      //одна из строк (лево/право)
      styledDiv {
        css(rowOfColumnsStyle)

        //контейнер для среза
        cutContainer(
          dependencies = dependencies(leftTop)
        )
        //TODO() пока известно что сверху справа пустой, поэтому заглушка
        styledDiv {
          css {
            display = Display.flex
            flexDirection = FlexDirection.row
            position = Position.relative
            width = 50.pct
          }
        }

      }

      styledDiv {
        css(rowOfColumnsStyle)

        cutContainer(dependencies = dependencies(leftBottom))
        cutContainer(dependencies = dependencies(rightBottom))
      }
    }
  }

  private fun dependencies(cut: Cut): CutContainer.Dependencies =
    object : CutContainer.Dependencies, Dependencies by props.dependencies {
      override val cut: Cut = cut
      override val cutOutput: (CutController.Output) -> Unit = ::cutOutput
      override val cutsInput: Observable<CutController.Input> = this@CutsContainerViewComponent.cutsInputObservable
    }

  private fun cutOutput(output: CutController.Output) {
    debugLog("cutOutputIncome $output")
    when (output) {
      is CutController.Output.SliceNumberChanged -> {
        //notify other cuts
        cutsInputObservable.onNext(
          CutController.Input.ExternalSliceNumberChanged(output.sliceNumber, output.cut)
        )
      }
    }
  }

  private fun updateState(model: Model) = setState { cutsModel = model }

  override fun componentWillUnmount() {
    disposable.dispose()
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val data: ResearchSlicesSizesData
    val cutsContainerInputs: Observable<CutsContainerController.Input>
    val cutsContainerOutput: (CutsContainerController.Output) -> Unit
    val cutsInput: Observable<CutController.Input>
    val researchRepository: ResearchRepository
    val researchId: Int
  }

  object GridContainerStyles : StyleSheet("CutsStyles", isStatic = true) {

    val columnOfRowsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.column
    }

    val rowOfColumnsStyle by css {
      flex(1.0)
      display = Display.flex
      flexDirection = FlexDirection.row
    }
  }
}

class CutsContainerState(
  var cutsModel: Model
) : RState

interface CutsContainerProps : RProps {
  var dependencies: CutsContainerViewComponent.Dependencies
}

fun RBuilder.cuts(dependencies: CutsContainerViewComponent.Dependencies) = child(
  CutsContainerViewComponent::class
) {
  attrs.dependencies = dependencies
}
