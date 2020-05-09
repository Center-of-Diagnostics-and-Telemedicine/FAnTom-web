package client.newmvi.shapes.store

import client.newmvi.shapes.store.ShapesStore.Intent
import client.newmvi.shapes.store.ShapesStore.State
import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.*
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.publish.PublishSubject
import model.*

class ShapesStoreImpl(
  val cutType: Int,
  private val hounsfieldDataLoader: HounsfieldDataLoader,
  private val changeCutTypeListener: PublishSubject<ChangeCutTypeModel>
) : ShapesStore {

  private val _states = BehaviorSubject(
    State(
      areas = listOf(),
      horizontalLine = initialLine(cutType, LineType.HORIZONTAL),
      verticalLine = initialLine(cutType, LineType.VERTICAL),
      moveRects = listOf()
    )
  )
  override val states: Observable<State> = _states
  private val state: State get() = _states.value

  private val disposables = CompositeDisposable()
  override val isDisposed: Boolean get() = disposables.isDisposed

  override fun dispose() {
    disposables.dispose()
    _states.onComplete()
  }

  override fun accept(intent: Intent) {
    execute(intent)?.also { disposables.add(it) }
  }

  private fun execute(intent: Intent): Disposable? =
    when (intent) {
      is Intent.AreasIncome -> {
        onResult(Effect.UpdateAreas(intent.areas))
        null
      }
      is Intent.UpdateLines -> {
        onResult(Effect.UpdateLines(intent.lines))
        null
      }
      is Intent.UpdateMouseData -> {
        if (intent.positionData.x > 0 && intent.positionData.y > 0)
          onResult(Effect.UpdatePositionData(intent.positionData))
        else
          onResult(Effect.RemovePositionData)
        null
      }
      is Intent.UpdateSliceNumber -> {
        onResult(Effect.UpdateSliceNumber(intent.sliceNumber))
        null
      }
      is Intent.UpdateMoveRects -> {
        onResult(Effect.UpdateMoveRects(intent.moveRects))
        null
      }
      is Intent.GetHounsfield -> {
        if (intent.positionData.x > 0 && intent.positionData.y > 0 && intent.positionData.z > 0) {
          loadHounsfield(intent.positionData)
        } else {
          onResult(Effect.RemovePositionData)
          null
        }
      }
      is Intent.ChangeCutType -> {
        changeCutTypeListener.onNext(ChangeCutTypeModel(intent.type.id, intent.cellModel))
        null
      }
      is Intent.Idle -> null
    }

  private fun loadHounsfield(positionData: PositionData): Disposable? =
    if (state.isLoading) {
      null
    } else {
      hounsfieldDataLoader
        .load(positionData.z, positionData.y, positionData.x)
        .observeOn(computationScheduler)
        .map {
          when (it) {
            is HounsfieldDataLoader.Result.Success -> Effect.UpdateHounsfield(it.huValue)
            is HounsfieldDataLoader.Result.Error -> Effect.HounsfieldLoadFailed(it.message)
            is HounsfieldDataLoader.Result.AxialValueError -> Effect.Error
            is HounsfieldDataLoader.Result.FrontalValueError -> Effect.Error
            is HounsfieldDataLoader.Result.SagittalValueError -> Effect.Error
            HounsfieldDataLoader.Result.SessionExpired -> Effect.Error
          }
        }
        .observeOn(mainScheduler)
        .subscribe(onSuccess = ::onResult)
    }

  private fun onResult(effect: Effect) {
    _states.onNext(Reducer(effect, _states.value))
  }

  private sealed class Effect {
    class UpdateAreas(val areas: List<CircleShape>) : Effect()
    class UpdateLines(val lines: Lines) : Effect()
    class UpdatePositionData(val positionData: PositionData) : Effect()
    class UpdateSliceNumber(val sliceNumber: Int) : Effect()
    class UpdateMoveRects(val moveRects: List<MoveRect>) : Effect()
    class UpdateHounsfield(val huValue: Double) : Effect()
    class HounsfieldLoadFailed(val message: String) : Effect()

    object Error : Effect()

    object LoadingStarted : Effect()
    object RemovePositionData : Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.UpdateAreas -> state.copy(areas = effect.areas)
        is Effect.UpdateLines -> state.copy(
          horizontalLine = effect.lines.horizontal,
          verticalLine = effect.lines.vertical,
          cubeColor = effect.lines.cubeColor
        )
        is Effect.UpdatePositionData -> state.copy(positionData = effect.positionData)
        is Effect.RemovePositionData -> state.copy(positionData = null, huValue = null)
        is Effect.UpdateSliceNumber -> state.copy(sliceNumber = effect.sliceNumber)
        is Effect.UpdateMoveRects -> state.copy(moveRects = effect.moveRects)
        is Effect.UpdateHounsfield -> state.copy(huValue = effect.huValue, isLoading = false)
        is Effect.HounsfieldLoadFailed -> state.copy(isLoading = false)
        Effect.LoadingStarted -> state.copy(isLoading = true)
        Effect.Error -> state.copy(isLoading = false)
      }
  }
}