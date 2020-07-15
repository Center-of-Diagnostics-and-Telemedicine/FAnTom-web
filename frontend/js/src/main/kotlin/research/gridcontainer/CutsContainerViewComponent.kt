package research.gridcontainer

import com.arkivanov.mvikotlin.core.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import controller.CutController
import controller.CutsContainerController
import controller.CutsContainerControllerImpl
import destroy
import kotlinx.css.*
import model.Cut
import model.CutType
import model.Grid
import model.ResearchSlicesSizesDataNew
import react.*
import repository.BrightnessRepository
import repository.MipRepository
import repository.ResearchRepository
import research.cut.CutContainer
import research.cut.cutContainer
import research.gridcontainer.CutsContainerViewComponent.GridContainerStyles.columnOfRowsStyle
import research.gridcontainer.CutsContainerViewComponent.GridContainerStyles.rowOfColumnsStyle
import resume
import styled.StyleSheet
import styled.css
import styled.styledDiv
import view.CutsContainerView.Model
import view.initialCutsContainerModel

class CutsContainerViewComponent(prps: CutsContainerProps) :
  RComponent<CutsContainerProps, CutsContainerState>(prps) {

  private val gridViewDelegate = CutsContainerViewProxy(::updateState)
  private val lifecycleRegistry = LifecycleRegistry()
  private val cutsInputObservable = BehaviorSubject<CutController.Input>(CutController.Input.Idle)
  private lateinit var controller: CutsContainerController
  private val disposable = CompositeDisposable()

  init {
    state = CutsContainerState(initialCutsContainerModel(props.dependencies.data.type))
  }

  override fun componentDidMount() {
    lifecycleRegistry.resume()
    controller = createController()
    controller.onViewCreated(gridViewDelegate, lifecycleRegistry)
    val dependencies = props.dependencies
    disposable.add(dependencies.cutsContainerInputs.subscribe { controller.input(it) })
    disposable.add(dependencies.cutsInput.subscribe { cutsInputObservable.onNext(it) })
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
        cutContainer(dependencies = dependencies(leftTop))
        if (rightTop.type == CutType.EMPTY) {
          styledDiv {
            css {
              display = Display.flex
              flexDirection = FlexDirection.row
              position = Position.relative
              width = 50.pct
            }
          }
        } else {
          cutContainer(dependencies = dependencies(rightTop))
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
    when (output) {
      is CutController.Output.SliceNumberChanged -> {
        //notify other cuts
        cutsInputObservable.onNext(
          CutController.Input.ExternalSliceNumberChanged(output.sliceNumber, output.cut)
        )
      }
      is CutController.Output.CircleDrawn -> {
        props.dependencies.cutsContainerOutput(
          CutsContainerController.Output.CircleDrawn(output.circle, output.sliceNumber, output.cut)
        )
      }
      is CutController.Output.SelectMark -> {
        props.dependencies.cutsContainerOutput(
          CutsContainerController.Output.SelectMark(output.mark)
        )
      }
      is CutController.Output.UnselectMark -> {
        props.dependencies.cutsContainerOutput(
          CutsContainerController.Output.UnselectMark(output.mark)
        )
      }
      is CutController.Output.CenterMark -> {
        cutsInputObservable.onNext(
          CutController.Input.ChangeSliceNumberByMarkCenter(output.mark)
        )
      }
      is CutController.Output.ContrastBrightnessChanged -> {
        props.dependencies.cutsContainerOutput(
          CutsContainerController.Output.ContrastBrightnessChanged(output.black, output.white)
        )
      }
      is CutController.Output.UpdateMark -> {
        props.dependencies.cutsContainerOutput(
          CutsContainerController.Output.UpdateMark(output.mark)
        )
      }
      is CutController.Output.UpdateMarkWithSave -> {
        props.dependencies.cutsContainerOutput(
          CutsContainerController.Output.UpdateMarkWithSave(output.mark)
        )
      }
    }.let { }
  }

  private fun updateState(model: Model) = setState { cutsModel = model }

  override fun componentWillUnmount() {
    disposable.dispose()
    lifecycleRegistry.destroy()
  }

  interface Dependencies {
    val storeFactory: StoreFactory
    val data: ResearchSlicesSizesDataNew
    val cutsContainerInputs: Observable<CutsContainerController.Input>
    val cutsContainerOutput: (CutsContainerController.Output) -> Unit
    val cutsInput: Observable<CutController.Input>
    val researchRepository: ResearchRepository
    val brightnessRepository: BrightnessRepository
    val mipRepository: MipRepository
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
