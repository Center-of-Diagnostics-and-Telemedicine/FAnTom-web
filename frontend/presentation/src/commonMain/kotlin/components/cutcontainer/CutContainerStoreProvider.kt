package components.cutcontainer

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.completable.observeOn
import com.badoo.reaktive.completable.subscribeOn
import com.badoo.reaktive.coroutinesinterop.completableFromCoroutine
import com.badoo.reaktive.observable.doOnAfterNext
import com.badoo.reaktive.observable.map
import com.badoo.reaktive.observable.mapIterable
import com.badoo.reaktive.observable.notNull
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import repository.MyBrightnessRepository
import repository.MyMarksRepository
import repository.MyMipRepository
import repository.MyResearchRepository
import store.cut.CutContainerStore
import store.cut.CutContainerStore.*
import store.cut.CutModel

class CutContainerStoreProvider(
  private val storeFactory: StoreFactory,
  private val brightnessRepository: MyBrightnessRepository,
  private val researchRepository: MyResearchRepository,
  private val marksRepository: MyMarksRepository,
  private val mipRepository: MyMipRepository,
  private val researchId: Int,
  private val plane: Plane,
  private val data: ResearchDataModel
) {

  val initialState = State(
    cutModel = CutModel(
      sliceNumber = plane.data.nImages / 2,
      blackValue = INITIAL_BLACK.toInt(),
      whiteValue = INITIAL_WHITE.toInt(),
      gammaValue = INITIAL_GAMMA,
      mip = Mip.initial,
      width = 0,
      height = 0
    ),
    marks = listOf(),
    mark = null,
    screenDimensionsModel = initialScreenDimensionsModel(),
    pointPosition = null
  )

  fun provide(): CutContainerStore =
    object : CutContainerStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CutContainerStore_${researchId}_${plane.type.intType}",
      initialState = initialState,
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::ExecutorImpl,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Label>() {

    override fun executeAction(action: Unit, getState: () -> State) {
      println("executeAction")
      brightnessRepository.black
        .map { Result.CutModelChanged(getState().cutModel.copy(blackValue = it)) }
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      brightnessRepository.white
        .map { Result.CutModelChanged(getState().cutModel.copy(whiteValue = it)) }
        .doOnAfterNext { publish(Label.CutModelChanged(it.cutModel)) }
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      brightnessRepository.gamma
        .map { Result.CutModelChanged(getState().cutModel.copy(gammaValue = it)) }
        .doOnAfterNext { publish(Label.CutModelChanged(it.cutModel)) }
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      mipRepository.mipMethod
        .map { Result.CutModelChanged(getState().cutModel.copy(mip = it)) }
        .doOnAfterNext { publish(Label.CutModelChanged(it.cutModel)) }
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      marksRepository.marks
        .notNull()
        .mapIterable { it.toMarkModel(data.markTypes) }
        .map(Result::Marks)
        .doOnAfterNext { result ->
          result
            .marks
            .mapNotNull {
              it.toShape(plane, getState().cutModel.sliceNumber)
            }
            .let {
              publish(Label.Shapes(it))
            }
        }
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )

      marksRepository.mark
        .map { it?.toMarkModel(data.markTypes) }
        .map(Result::CurrentMark)
        .subscribeScoped(
          onNext = ::dispatch,
          onError = ::handleError
        )
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.ChangeSliceNumber -> handleNewSliceNumber(intent.sliceNumber, getState())
        is Intent.HandleNewShape -> handleNewShape(intent.shape, getState())
        is Intent.UpdateScreenDimensions -> handleDimensions(intent.dimensions, getState())
        is Intent.PointPosition -> handlePointPosition(intent.pointPosition, getState())
      }.let { }
    }

    private fun handleNewSliceNumber(sliceNumber: Int, state: State) {
      val newCutModel = state.cutModel.copy(sliceNumber = sliceNumber)
      dispatch(Result.CutModelChanged(newCutModel))
      publish(Label.CutModelChanged(newCutModel))
    }

    private fun handleNewShape(shape: Shape, state: State) {
      plane.getMarkToSave(shape, state.cutModel.sliceNumber)?.let { markToSave ->
        completableFromCoroutine {
          marksRepository.saveMark(markToSave, researchId)
        }
          .subscribeOn(ioScheduler)
          .observeOn(mainScheduler)
          .subscribeScoped(onError = ::handleError)
      }
    }

    private fun handleDimensions(dimensions: ScreenDimensionsModel, state: State) {
      dispatch(Result.ScreenDimensionsChanged(dimensions))
      publish(Label.ScreenDimensions(dimensions))

      val newCutModel = state.cutModel.copy(
        width = dimensions.originalScreenWidth,
        height = dimensions.originalScreenHeight
      )
      dispatch(Result.CutModelChanged(newCutModel))
      publish(Label.CutModelChanged(newCutModel))
    }

    private fun handlePointPosition(pointPosition: PointPositionModel?, state: State) {
      dispatch(Result.MousePosition(pointPosition))
      publish(Label.MousePosition(pointPosition))
    }

    private fun handleError(error: Throwable) {
      error.printStackTrace()
    }
  }

  private sealed class Result : JvmSerializable {
    data class CutModelChanged(val cutModel: CutModel) : Result()
    data class Marks(val marks: List<MarkModel>) : Result()
    data class CurrentMark(val mark: MarkModel?) : Result()
    data class ScreenDimensionsChanged(val dimensions: ScreenDimensionsModel) : Result()
    data class MousePosition(val pointPosition: PointPosition?) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.CutModelChanged -> copy(cutModel = result.cutModel)
        is Result.Marks -> copy(marks = result.marks)
        is Result.CurrentMark -> copy(mark = result.mark)
        is Result.ScreenDimensionsChanged -> copy(screenDimensionsModel = result.dimensions)
        is Result.MousePosition -> copy(pointPosition = result.pointPosition)
      }
  }
}