package store.shapes

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.atomic.AtomicInt
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import store.shapes.ShapesStore.*

abstract class ShapesStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Cut,
  private val research: Research
) {

  val initialState: State = State(
    horizontalCoefficient = 0.5,
    verticalCoefficient = 0.5,
    sliceNumber = cut.data.n_images / 2,
    position = null,
    shapes = listOf(),
    hounsfield = null,
    marks = listOf(),
    expertMarks = listOf(),
    rects = listOf(),
    moveRect = null,
  )

  fun create(): ShapesStore =
    object : ShapesStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "ShapesStoreType${cut.type.intType}Id${research.id}index${storeIndex.addAndGet(1)}",
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
    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    data class HorizontalCoefficientChanged(val coefficient: Double) : Result()
    data class VerticalCoefficientChanged(val coefficient: Double) : Result()
    data class PointPositionChanged(val position: PointPosition?) : Result()
    data class HounsfieldChanged(val hu: Double) : Result()
    data class Marks(val marks: List<MarkModel>) : Result()
    data class ExpertMarks(val expertMarks: List<MarkModel>) : Result()
    data class Shapes(val shapes: List<Shape>) : Result()
    data class Rects(val rects: List<Rect>) : Result()
    data class SideRectInMove(val moveRect: Rect?) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.HorizontalCoefficientChanged -> copy(horizontalCoefficient = result.coefficient)
        is Result.VerticalCoefficientChanged -> copy(verticalCoefficient = result.coefficient)
        is Result.PointPositionChanged -> copy(position = result.position)
        is Result.Shapes -> copy(shapes = result.shapes)
        is Result.Rects -> copy(rects = result.rects)
        is Result.HounsfieldChanged -> copy(hounsfield = result.hu.toInt())
        is Result.Marks -> copy(marks = result.marks)
        is Result.ExpertMarks -> copy(expertMarks = result.expertMarks)
        is Result.SideRectInMove -> copy(moveRect = result.moveRect)
      }
  }
}
