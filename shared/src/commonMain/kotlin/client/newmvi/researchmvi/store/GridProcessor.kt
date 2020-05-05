package client.newmvi.researchmvi.store

import model.*
import model.*

interface GridProcessor {

  fun init(researchData: ResearchSlicesSizesData): GridModel

  fun changeGridType(
    newType: CutsGridType,
    currentModel: GridModel,
    sliceSizesData: ResearchSlicesSizesData?
  ): GridModel

  fun changeCutType(
    newCutType: ChangeCutTypeModel,
    currentModel: GridModel,
    sliceSizesData: ResearchSlicesSizesData?
  ): GridModel

  fun openCutFullMode(cellModel: CellModel): GridModel

  fun closeCutFullMode(oldModel: GridModel): GridModel
}

class GridProcessorImpl : GridProcessor {

  override fun init(researchData: ResearchSlicesSizesData): GridModel {
    return makeThree(researchSlicesSizesData = researchData)
  }

  override fun changeGridType(
    newType: CutsGridType,
    currentModel: GridModel,
    sliceSizesData: ResearchSlicesSizesData?
  ): GridModel {
    return when (newType) {
      CutsGridType.SINGLE -> makeSingleContainer(sliceSizesData)
      CutsGridType.TWO_VERTICAL -> makeTwoVertical(sliceSizesData)
      CutsGridType.TWO_HORIZONTAL -> makeTwoHorizontal(sliceSizesData)
      CutsGridType.THREE -> makeThree(sliceSizesData)
      CutsGridType.FOUR -> TODO()
    }
  }

  override fun changeCutType(
    newCutType: ChangeCutTypeModel,
    currentModel: GridModel,
    sliceSizesData: ResearchSlicesSizesData?
  ): GridModel {
    val cellModel = newCutType.cellModel
    return currentModel.copy(cells = currentModel.cells.map {
      return@map if (it.position == cellModel.position) {
        it.copy(
          cutType = newCutType.cutType,
          position = cellModel.position,
          sliceSizeData = getSliceSizeDataByCutType(newCutType.cutType, sliceSizesData)
            ?: initialSlicesSizeData(),
          cutTypeModelContainer = updateCutTypeModel(it.cutTypeModelContainer, newCutType.cutType)
        )
      } else it
    })
  }

  override fun openCutFullMode(
    cellModel: CellModel
  ): GridModel {
    return GridModel(type = CutsGridType.SINGLE, cells = listOf(cellModel))
  }

  override fun closeCutFullMode(oldModel: GridModel): GridModel {
    return oldModel
  }

  private fun makeSingleContainer(researchSlicesSizesData: ResearchSlicesSizesData?): GridModel {
    return GridModel(
      type = CutsGridType.SINGLE,
      cells = listOf(
        CellModel(
          SLYCE_TYPE_FRONTAL,
          Position.LEFT_TOP,
          researchSlicesSizesData?.frontal ?: initialSlicesSizeData(),
          CutTypeModelContainer(frontal, listOf(axial, sagittal))
        )
      )
    )
  }

  private fun makeTwoVertical(researchSlicesSizesData: ResearchSlicesSizesData?): GridModel {
    return GridModel(
      type = CutsGridType.TWO_VERTICAL,
      cells = listOf(
        CellModel(
          SLYCE_TYPE_AXIAL,
          Position.LEFT_TOP,
          researchSlicesSizesData?.axial ?: initialSlicesSizeData(),
          CutTypeModelContainer(axial, listOf(sagittal))
        ),
        CellModel(
          SLYCE_TYPE_FRONTAL,
          Position.LEFT_BOTTOM,
          researchSlicesSizesData?.frontal ?: initialSlicesSizeData(),
          CutTypeModelContainer(frontal, listOf(sagittal))
        )
      )
    )
  }

  private fun makeTwoHorizontal(researchSlicesSizesData: ResearchSlicesSizesData?): GridModel {
    return GridModel(
      type = CutsGridType.TWO_HORIZONTAL,
      cells = listOf(
        CellModel(
          SLYCE_TYPE_FRONTAL,
          Position.LEFT_TOP,
          researchSlicesSizesData?.frontal ?: initialSlicesSizeData(),
          CutTypeModelContainer(frontal, listOf(sagittal))
        ),
        CellModel(
          SLYCE_TYPE_AXIAL,
          Position.RIGHT_TOP,
          researchSlicesSizesData?.axial ?: initialSlicesSizeData(),
          CutTypeModelContainer(axial, listOf(sagittal))
        )
      )
    )
  }

  private fun makeThree(researchSlicesSizesData: ResearchSlicesSizesData?): GridModel {
    return GridModel(
      type = CutsGridType.THREE,
      cells = listOf(
        CellModel(
          SLYCE_TYPE_AXIAL,
          Position.LEFT_TOP,
          researchSlicesSizesData?.axial ?: initialSlicesSizeData(),
          CutTypeModelContainer(axial, listOf())
        ),
        CellModel(
          SLYCE_TYPE_FRONTAL,
          Position.LEFT_BOTTOM,
          researchSlicesSizesData?.frontal ?: initialSlicesSizeData(),
          CutTypeModelContainer(frontal, listOf())
        ),
        CellModel(
          SLYCE_TYPE_SAGITTAL,
          Position.RIGHT_BOTTOM,
          researchSlicesSizesData?.sagittal ?: initialSlicesSizeData(),
          CutTypeModelContainer(sagittal, listOf())
        )
      )
    )
  }

  private fun getSliceSizeDataByCutType(
    cutType: Int,
    sliceSizesData: ResearchSlicesSizesData?
  ): SliceSizeData? {
    return when (cutType) {
      SLYCE_TYPE_AXIAL -> sliceSizesData?.axial
      SLYCE_TYPE_FRONTAL -> sliceSizesData?.frontal
      SLYCE_TYPE_SAGITTAL -> sliceSizesData?.sagittal
      else -> throw NotImplementedError("может стоит добавить новый тип среза?")
    }
  }


  private fun updateCutTypeModel(
    oldModelContainer: CutTypeModelContainer,
    newCutType: Int
  ): CutTypeModelContainer {
    val cutType = getCutTypeByType(newCutType)
    val newAvailableTypes =
      oldModelContainer
        .availableOtherTypesForCut
        .plus(oldModelContainer.currentCutType)
        .minus(cutType)

    return oldModelContainer.copy(
      currentCutType = cutType,
      availableOtherTypesForCut = newAvailableTypes
    )
  }

  private fun getCutTypeByType(type: Int): CutType {
    return when (type) {
      SLYCE_TYPE_AXIAL -> axial
      SLYCE_TYPE_FRONTAL -> frontal
      SLYCE_TYPE_SAGITTAL -> sagittal
      else -> axial
    }
  }

}