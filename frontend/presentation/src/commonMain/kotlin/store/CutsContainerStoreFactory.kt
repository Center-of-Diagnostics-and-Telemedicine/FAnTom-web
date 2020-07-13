package store

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.scheduler.ioScheduler
import com.badoo.reaktive.scheduler.mainScheduler
import com.badoo.reaktive.single.observeOn
import com.badoo.reaktive.single.singleFromFunction
import com.badoo.reaktive.single.subscribeOn
import model.*
import store.gridcontainer.CutsContainerStore.Intent
import store.gridcontainer.CutsContainerStore.State
import store.gridcontainer.CutsContainerStoreAbstractFactory

internal class CutsContainerStoreFactory(
  storeFactory: StoreFactory,
  val data: ResearchSlicesSizesDataNew
) : CutsContainerStoreAbstractFactory(
  storeFactory = storeFactory,
  data = data
) {

  override fun createExecutor(): Executor<Intent, Unit, State, Result, Nothing> = ExecutorImpl()

  private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Result, Nothing>() {
    override fun executeAction(action: Unit, getState: () -> State) {
      val types = initialFourGridTypes(data.type)
      singleFromFunction {
        Result.Loaded(
          items = buildCuts(types),
          grid = initialFourGrid(data.type)
        )
      }
        .subscribeOn(ioScheduler)
        .observeOn(mainScheduler)
        .subscribeScoped(
          isThreadLocal = true,
          onSuccess = ::dispatch
        )
    }

    private fun initialFourGridTypes(type: ResearchType): List<CutType> {
      return when (type) {
        ResearchType.CT -> listOf(
          CutType.CT_AXIAL,
          CutType.EMPTY,
          CutType.CT_FRONTAL,
          CutType.CT_SAGITTAL
        )
        ResearchType.MG -> listOf(
          CutType.MG_RCC,
          CutType.MG_LCC,
          CutType.MG_RMLO,
          CutType.MG_LMLO
        )
        ResearchType.DX -> listOf(
          CutType.DX_GENERIC,
          CutType.DX_POSTERO_ANTERIOR,
          CutType.DX_LEFT_LATERAL,
          CutType.DX_RIGHT_LATERAL
        )
      }
    }

    override fun executeIntent(intent: Intent, getState: () -> State) {
      when (intent) {
        is Intent.HandleGridChanged ->
          dispatch(Result.Loaded(items = buildCuts(intent.grid.types), grid = intent.grid))
      }.let {}
    }

    private fun buildCuts(types: List<CutType>): List<Cut> =
      types.mapNotNull {
        buildCut(it)
      }

    private fun buildCut(type: CutType): Cut? {
      return when (type) {
        CutType.EMPTY -> {
          Cut(
            type = CutType.EMPTY,
            data = ModalityModel(0, 0, 0.0, 0.0, 0, 0, 0),
            color = "",
            verticalCutData = null,
            horizontalCutData = null
          )
        }
        CutType.CT_AXIAL -> {
          Cut(
            type = CutType.CT_AXIAL,
            data = data.modalities[SLICE_TYPE_CT_AXIAL]
              ?: error("CutsContainerStoreFactory: AXIAL NOT FOUND IN DATA"),
            color = axialColor,
            verticalCutData = CutData(
              type = CutType.CT_FRONTAL,
              data = data.modalities[SLICE_TYPE_CT_FRONTAL]
                ?: error("CutsContainerStoreFactory: FRONTAL NOT FOUND IN DATA"),
              color = frontalColor
            ),
            horizontalCutData = CutData(
              type = CutType.CT_SAGITTAL,
              data = data.modalities[SLICE_TYPE_CT_SAGITTAL]
                ?: error("CutsContainerStoreFactory: SAGITTAL NOT FOUND IN DATA"),
              color = sagittalColor
            )
          )
        }
        CutType.CT_FRONTAL -> Cut(
          type = CutType.CT_FRONTAL,
          data = data.modalities[SLICE_TYPE_CT_FRONTAL]
            ?: error("CutsContainerStoreFactory: FRONTAL NOT FOUND IN DATA"),
          color = frontalColor,
          verticalCutData = CutData(
            type = CutType.CT_AXIAL,
            data = data.modalities[SLICE_TYPE_CT_AXIAL]
              ?: error("CutsContainerStoreFactory: AXIAL NOT FOUND IN DATA"),
            color = axialColor
          ),
          horizontalCutData = CutData(
            type = CutType.CT_SAGITTAL,
            data = data.modalities[SLICE_TYPE_CT_SAGITTAL]
              ?: error("CutsContainerStoreFactory: SAGITTAL NOT FOUND IN DATA"),
            color = sagittalColor
          )
        )
        CutType.CT_SAGITTAL -> Cut(
          type = CutType.CT_SAGITTAL,
          data = data.modalities[SLICE_TYPE_CT_SAGITTAL]
            ?: error("CutsContainerStoreFactory: SAGITTAL NOT FOUND IN DATA"),
          color = sagittalColor,
          verticalCutData = CutData(
            type = CutType.CT_AXIAL,
            data = data.modalities[SLICE_TYPE_CT_AXIAL]
              ?: error("CutsContainerStoreFactory: AXIAL NOT FOUND IN DATA"),
            color = axialColor
          ),
          horizontalCutData = CutData(
            type = CutType.CT_FRONTAL,
            data = data.modalities[SLICE_TYPE_CT_FRONTAL]
              ?: error("CutsContainerStoreFactory: FRONTAL NOT FOUND IN DATA"),
            color = frontalColor
          )
        )
        CutType.MG_RCC -> Cut(
          type = CutType.MG_RCC,
          data = data.modalities[SLICE_TYPE_MG_RCC]!!,
          color = rcc_color,
          verticalCutData = null,
          horizontalCutData = null
        )
        CutType.MG_LCC -> Cut(
          type = CutType.MG_LCC,
          data = data.modalities[SLICE_TYPE_MG_LCC]!!,
          color = lcc_color,
          verticalCutData = null,
          horizontalCutData = null
        )
        CutType.MG_RMLO -> Cut(
          type = CutType.MG_RMLO,
          data = data.modalities[SLICE_TYPE_MG_RMLO]!!,
          color = rmlo_color,
          verticalCutData = null,
          horizontalCutData = null
        )
        CutType.MG_LMLO -> Cut(
          type = CutType.MG_LMLO,
          data = data.modalities[SLICE_TYPE_MG_LMLO]!!,
          color = lmlo_color,
          verticalCutData = null,
          horizontalCutData = null
        )
        CutType.DX_GENERIC -> Cut(
          type = CutType.DX_GENERIC,
          data = data.modalities[SLICE_TYPE_DX_GENERIC]!!,
          color = generic_color,
          verticalCutData = null,
          horizontalCutData = null
        )
        CutType.DX_POSTERO_ANTERIOR -> Cut(
          type = CutType.DX_POSTERO_ANTERIOR,
          data = data.modalities[SLICE_TYPE_DX_POSTERO_ANTERIOR]!!,
          color = postero_color,
          verticalCutData = null,
          horizontalCutData = null
        )
        CutType.DX_LEFT_LATERAL -> Cut(
          type = CutType.DX_LEFT_LATERAL,
          data = data.modalities[SLICE_TYPE_DX_LEFT_LATERAL]!!,
          color = left_lateral_color,
          verticalCutData = null,
          horizontalCutData = null
        )
        CutType.DX_RIGHT_LATERAL -> Cut(
          type = CutType.DX_RIGHT_LATERAL,
          data = data.modalities[SLICE_TYPE_DX_RIGHT_LATERAL]!!,
          color = right_lateral_color,
          verticalCutData = null,
          horizontalCutData = null
        )
      }
    }
  }

}
