package store.shapes

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.Circle
import model.Cut
import model.MarkDomain
import model.PointPosition
import store.shapes.ShapesStore.*

abstract class ShapesStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Cut,
  private val researchId: Int
) {

  val initialState: State = State(
    horizontalCoefficient = 0.5,
    verticalCoefficient = 0.5,
    sliceNumber = cut.data!!.maxFramesSize / 2,
    position = null,
    circles = listOf(),
    hounsfield = null,
    marks = listOf()
  )

  fun create(): ShapesStore =
    object : ShapesStore,
      Store<Intent, State, Label> by storeFactory.create(
        name = "ShapesStoreType${cut.type.intType}Id${researchId}",
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
    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    data class HorizontalLineChanged(val coefficient: Double) : Result()
    data class VerticalLineChanged(val coefficient: Double) : Result()
    data class PointPositionChanged(val position: PointPosition?) : Result()
    data class HounsfieldChanged(val hu: Double) : Result()
    data class Marks(val marks: List<MarkDomain>) : Result()
    data class Circles(val list: List<Circle>) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.HorizontalLineChanged -> copy(horizontalCoefficient = result.coefficient)
        is Result.VerticalLineChanged -> copy(verticalCoefficient = result.coefficient)
        is Result.PointPositionChanged -> copy(position = result.position)
        is Result.Circles -> copy(circles = result.list)
        is Result.HounsfieldChanged -> copy(hounsfield = result.hu.toInt())
        is Result.Marks -> copy(marks = result.marks)
      }
  }
}
