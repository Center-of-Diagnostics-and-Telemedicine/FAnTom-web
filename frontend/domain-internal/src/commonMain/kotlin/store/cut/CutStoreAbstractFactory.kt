package store.cut

import com.arkivanov.mvikotlin.core.store.*
import com.arkivanov.mvikotlin.core.utils.JvmSerializable
import com.badoo.reaktive.utils.ensureNeverFrozen
import model.*
import store.cut.CutStore.*

abstract class CutStoreAbstractFactory(
  private val storeFactory: StoreFactory,
  private val cut: Cut,
  private val researchId: Int
) {

  val initialState: State = State(
    sliceNumber = cut.data!!.maxFramesSize / 2,
    slice = "",
    black = INITIAL_BLACK.toInt(),
    white = INITIAL_WHITE.toInt(),
    gamma = INITIAL_GAMMA,
    mipMethod = Mip.No,
    mipValue = INITIAL_MIP_VALUE,
    loading = false,
    error = ""
  )

  fun create(): CutStore =
    object : CutStore, Store<Intent, State, Label> by storeFactory.create(
      name = "CutStoreType${cut.type.intType}Id${researchId}",
      initialState = initialState,
      bootstrapper = SimpleBootstrapper(Unit),
      executorFactory = ::createExecutor,
      reducer = ReducerImpl
    ) {
      init {
        ensureNeverFrozen()
      }
    }

  protected abstract fun createExecutor(): Executor<Intent, Unit, State, Result, Label>

  protected sealed class Result : JvmSerializable {
    object Loading : Result()
    data class Loaded(val slice: String) : Result()
    data class Error(val message: String) : Result()

    data class SliceNumberChanged(val sliceNumber: Int) : Result()
    data class BlackChanged(val blackValue: Int) : Result()
    data class WhiteChanged(val whiteValue: Int) : Result()
    data class GammaChanged(val gammaValue: Double) : Result()
    data class MipChanged(val mip: Mip) : Result()
    data class MipValueChanged(val mipValue: Int) : Result()
    data class PresetChanged(val black: Int, val white: Int) : Result()
  }

  private object ReducerImpl : Reducer<State, Result> {
    override fun State.reduce(result: Result): State =
      when (result) {
        is Result.Loading -> copy(loading = true)
        is Result.Loaded -> copy(
          loading = false,
          error = "",
          slice = result.slice
        )
        is Result.Error -> copy(error = result.message)
        is Result.SliceNumberChanged -> copy(sliceNumber = result.sliceNumber)
        is Result.BlackChanged -> copy(black = result.blackValue)
        is Result.WhiteChanged -> copy(white = result.whiteValue)
        is Result.GammaChanged -> copy(gamma = result.gammaValue)
        is Result.MipChanged -> copy(mipMethod = result.mip)
        is Result.MipValueChanged -> copy(mipValue = result.mipValue)
        is Result.PresetChanged -> copy(black = result.black, white = result.white)
      }
  }
}