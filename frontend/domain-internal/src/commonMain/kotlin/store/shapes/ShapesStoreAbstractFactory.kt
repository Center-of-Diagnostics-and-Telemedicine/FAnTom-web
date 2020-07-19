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
  private val researchId: Int
) {

  val initialState: State = State(
    horizontalCoefficient = 0.5,
    verticalCoefficient = 0.5,
    sliceNumber = cut.data.n_images / 2,
    position = null,
    circles = listOf(),
    hounsfield = null,
    marks = listOf(),
    rects = listOf()
  )

  fun create(): ShapesStore =
    object : ShapesStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "ShapesStoreType${cut.type.intType}Id${researchId}index${storeIndex.addAndGet(1)}",
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
    data class Marks(val marks: List<MarkDomain>) : Result()
    data class Circles(val circles: List<Circle>) : Result()
    data class Rects(val rects: List<Rect>) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.HorizontalCoefficientChanged -> copy(horizontalCoefficient = result.coefficient)
        is Result.VerticalCoefficientChanged -> copy(verticalCoefficient = result.coefficient)
        is Result.PointPositionChanged -> copy(position = result.position)
        is Result.Circles -> copy(circles = result.circles)
        is Result.Rects -> copy(rects = result.rects)
        is Result.HounsfieldChanged -> copy(hounsfield = result.hu.toInt())
        is Result.Marks -> copy(marks = result.marks)
      }
  }
}
