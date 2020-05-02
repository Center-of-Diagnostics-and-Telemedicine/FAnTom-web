package client.newmvi.draw.store

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.subject.Subject
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import client.newmvi.draw.store.DrawStore.Intent
import client.newmvi.draw.store.DrawStore.State
import model.Circle
import model.MouseClickData
import model.MouseData
import model.MouseMoveData
import kotlin.math.pow
import kotlin.math.sqrt

class DrawStoreImpl(
  private val areaObservable: Subject<Circle>,
  private val mouseDataObservable: Subject<MouseData>,
  private val contrastBrightnessListener: Subject<MouseMoveData>,
  private val contrastBrightnessEndListener: Subject<MouseData>,
  private val mouseMoveListener: Subject<MouseMoveData>,
  private val mouseClickObservable: Subject<MouseClickData>,
  private val mouseDownListener: Subject<MouseClickData>,
  private val mouseUpListener: Subject<MouseData>,
  private val sliceNumberMoveListener: Subject<Int>
) : DrawStore {

  private val _states = BehaviorSubject(State(-1.0, -1.0, 0.0))
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
      is Intent.StartDraw -> {
//        onResult(Effect.StartDraw(intent.startX, intent.startY))
        null
      }
      is Intent.StartContrastBrightness -> {
        onResult(Effect.StartContrastBrightness(intent.startX, intent.startY))
        null
      }
      is Intent.Move -> {
        mouseDataObservable.onNext(MouseData(intent.x, intent.y))
        when {
          state.isDrawing -> onResult(Effect.Drawing(intent.x, intent.y))
          state.isContrastBrightness -> {
            contrastBrightnessListener.onNext(
              MouseMoveData(
                deltaX = intent.x - state.startX,
                deltaY = intent.y - state.startY
              )
            )
            onResult(Effect.ContrastBrightness(intent.x, intent.y))
          }
          else -> {
            mouseMoveListener.onNext(
              MouseMoveData(deltaX = intent.x - state.startX, deltaY = intent.y - state.startY)
            )
            onResult(Effect.MouseMove(intent.x, intent.y))
          }
        }
        null
      }
      is Intent.MouseUp -> {
        when {
          state.isDrawing -> areaObservable.onNext(state.circle())
          state.isContrastBrightness -> {
            contrastBrightnessEndListener.onNext(MouseData(intent.x, intent.y))
          }
        }
        mouseUpListener.onNext(MouseData(intent.x, intent.y))
        onResult(Effect.Idle)
        null
      }
      is Intent.MouseOut -> {
        mouseDataObservable.onNext(MouseData(-1.0, -1.0))
        onResult(Effect.Idle)
        null
      }
      is Intent.MouseClick -> {
        mouseClickObservable.onNext(MouseClickData(intent.x, intent.y, intent.altKey))
        null
      }
      is Intent.None -> {
        null
      }
      is Intent.StartMouseMove -> {
        mouseDownListener.onNext(MouseClickData(intent.startX, intent.startY, intent.shiftKey))
        onResult(Effect.StartMove(intent.startX, intent.startY))
        null
      }

      is Intent.ChangeSliceNumber -> {
        sliceNumberMoveListener.onNext(intent.deltaY)
        null
      }
    }

  private fun onResult(effect: Effect) {
    _states.onNext(Reducer(effect, _states.value))
  }

  private sealed class Effect {
    class StartDraw(val startX: Double, val startY: Double) : Effect()
    class Drawing(val newX: Double, val newY: Double) : Effect()
    class StartContrastBrightness(val startX: Double, val startY: Double) : Effect()
    class ContrastBrightness(val x: Double, val y: Double) : Effect()
    class StartMove(val startX: Double, val startY: Double) : Effect()
    class MouseMove(val x: Double, val y: Double) : Effect()

    object Idle : Effect()
  }

  private object Reducer {
    operator fun invoke(effect: Effect, state: State): State =
      when (effect) {
        is Effect.StartDraw -> state.copy(
          startX = effect.startX,
          startY = effect.startY,
          isDrawing = true
        )
        is Effect.Drawing -> {
          val xSqr = effect.newX - state.startX
          val ySqr = effect.newY - state.startY
          state.copy(radius = sqrt((xSqr).pow(2) + (ySqr).pow(2)))
        }
        is Effect.StartContrastBrightness -> state.copy(
          startX = effect.startX,
          startY = effect.startY,
          isContrastBrightness = true
        )
        is Effect.ContrastBrightness -> state.copy(
          startX = effect.x,
          startY = effect.y
        )
        is Effect.Idle -> state.copy(
          isDrawing = false,
          isContrastBrightness = false,
          startY = 0.0,
          startX = 0.0,
          radius = 0.0
        )
        is Effect.StartMove -> state.copy(
          isMoving = true,
          startX = effect.startX,
          startY = effect.startY
        )
        is Effect.MouseMove -> state.copy(
          startX = effect.x,
          startY = effect.y
        )
      }
  }
}