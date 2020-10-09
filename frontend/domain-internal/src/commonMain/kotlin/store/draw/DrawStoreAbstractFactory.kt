package store.draw

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.atomic.AtomicInt
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
    dicomRadiusHorizontal = 0.0,
    dicomRadiusVertical = 0.0,
    isDrawingEllipse = false,
    isMoving = false,
    isContrastBrightness = false
  )

  fun create(): DrawStore =
    object : DrawStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "DrawStoreType${cut.type.intType}Id${researchId}index${storeIndex.addAndGet(1)}",
        initialState = initialState,
        executorFactory = ::createExecutor,
        reducer = ReducerImpl
      ) {
      init {
        ensureNeverFrozen()
      }
    }

  private companion object {
    private val storeIndex = AtomicInt(0)
  }

  protected abstract fun createExecutor(): Executor<Intent, Nothing, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    data class StartDrawEllipse(val startDicomX: Double, val startDicomY: Double) : Result()
    data class StartDrawRectangle(val startDicomX: Double, val startDicomY: Double) : Result()
    data class MultiPlanarDrawing(val newDicomX: Double, val newDicomY: Double) : Result()
    data class ExternalDrawing(val dicomX: Double, val dicomY: Double) : Result()
    data class StartContrastBrightness(val startDicomX: Double, val startDicomY: Double) : Result()
    data class ContrastBrightness(val dicomX: Double, val dicomY: Double) : Result()
    data class StartClick(val startDicomX: Double, val startDicomY: Double) : Result()
    data class MouseMove(val dicomX: Double, val dicomY: Double) : Result()
    data class MouseMoveInClick(val dicomX: Double, val dicomY: Double) : Result()
    data class PlanarDrawing(val dicomX: Double, val dicomY: Double) : Result()

    object Idle : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.StartDrawEllipse -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isDrawingEllipse = true
        )
        is Result.StartDrawRectangle -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isDrawingRectangle = true
        )
        is Result.StartContrastBrightness -> copy(
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY,
          isContrastBrightness = true
        )
        is Result.StartClick -> copy(
          isMoving = true,
          startDicomX = result.startDicomX,
          startDicomY = result.startDicomY
        )
        is Result.MultiPlanarDrawing -> {
          val xSqr = result.newDicomX - startDicomX
          val ySqr = result.newDicomY - startDicomY
          val radius = sqrt((xSqr).pow(2) + (ySqr).pow(2))
          copy(dicomRadiusHorizontal = radius, dicomRadiusVertical = radius)
        }
        is Result.PlanarDrawing -> copy(
          dicomRadiusHorizontal = (result.dicomX - startDicomX),
          dicomRadiusVertical = (result.dicomY - startDicomY)
        )
        is Result.ContrastBrightness -> copy(
          startDicomX = result.dicomX,
          startDicomY = result.dicomY
        )
        is Result.MouseMove -> copy(
          startDicomX = result.dicomX - startDicomX,
          startDicomY = result.dicomY - startDicomY
        )
        is Result.MouseMoveInClick -> copy(
          startDicomX = result.dicomX,
          startDicomY = result.dicomY
        )
        Result.Idle -> copy(
          startDicomX = 0.0,
          startDicomY = 0.0,
          dicomRadiusHorizontal = 0.0,
          dicomRadiusVertical = 0.0,
          isDrawingEllipse = false,
          isDrawingRectangle = false,
          isContrastBrightness = false,
          isMoving = false
        )
        is Result.ExternalDrawing -> {
          val radius = sqrt((result.dicomX).pow(2) + (result.dicomY).pow(2))
          copy(dicomRadiusHorizontal = radius, dicomRadiusVertical = radius)
        }
      }
  }
}
