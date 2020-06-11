package store.draw

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Cut
import store.draw.DrawStore.*
import kotlin.math.pow
import kotlin.math.sqrt

abstract class DrawStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Cut,
  private val researchId: Int
) {

  val initialState: State = State(
    startDicomX = 0.0,
    startDicomY = 0.0,
    dicomRadius = 0.0,
    isDrawing = false,
    isMoving = false,
    isContrastBrightness = false
  )

  fun create(): DrawStore =
    object : DrawStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "DrawStoreType${cut.type.intType}Id${researchId}",
        initialState = initialState,
        executorFactory = ::createExecutor,
        reducer = ReducerImpl
      ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    class StartDraw(val startDicomX: Double, val startDicomY: Double) : Result()
    class Drawing(val newDicomX: Double, val newDicomY: Double) : Result()
    class ExternalDrawing(val dicomX: Double, val dicomY: Double) : Result()
    class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Result()
    class ContrastBrightness(val dicomX: Double, val dicomY: Double) : Result()
    class StartMove(val startDicomX: Double, val startDicomY: Double) : Result()
    class MouseMove(val dicomX: Double, val dicomY: Double) : Result()
    object Idle : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.StartDraw -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isDrawing = true
        )
        is Result.StartContrastBrightness -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isContrastBrightness = true
        )
        is Result.StartMove -> copy(
          isMoving = true,
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY
        )
        is Result.Drawing -> {
          val xSqr = result.newDicomX - startDicomX
          val ySqr = result.newDicomY - startDicomY
          copy(dicomRadius = sqrt((xSqr).pow(2) + (ySqr).pow(2)))
        }
        is Result.ContrastBrightness -> copy(
          startDicomX = result.dicomX,
          startDicomY = result.dicomY
        )
        is Result.MouseMove -> copy(
          startDicomX = result.dicomX,
          startDicomY = result.dicomY
        )
        Result.Idle -> copy(
          startDicomX = 0.0,
          startDicomY = 0.0,
          dicomRadius = 0.0,
          isDrawing = false,
          isContrastBrightness = false,
          isMoving = false
        )
        is Result.ExternalDrawing -> {
          copy(dicomRadius = sqrt((result.dicomX).pow(2) + (result.dicomY).pow(2)))
        }
      }
  }
}
