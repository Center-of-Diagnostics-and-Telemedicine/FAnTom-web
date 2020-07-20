package model

sealed class GridType {
  object Single : GridType()
  object TwoVertical : GridType()
  object TwoHorizontal : GridType()
  object Four : GridType()
}

sealed class Grid(val types: List<CutType>) {
  data class Single(
    val cut: CutType
  ) : Grid(listOf(cut))

  data class TwoVertical(
    val top: CutType,
    val bottom: CutType
  ) : Grid(listOf(top, bottom))

  data class TwoHorizontal(
    val left: CutType,
    val right: CutType
  ) : Grid(listOf(left, right))

  data class Four(
    val topLeft: CutType,
    val topRight: CutType,
    val bottomLeft: CutType,
    val bottomRight: CutType
  ) : Grid(listOf(topLeft, topRight, bottomLeft, bottomRight))

  companion object {
    fun build(type: GridType, researchType: ResearchType): Grid {
      return when (type) {
        GridType.Single -> initialSingleGrid(researchType)
        GridType.TwoVertical -> initialTwoVerticalGrid(researchType)
        GridType.TwoHorizontal -> initialTwoHorizontalGrid(researchType)
        GridType.Four -> initialFourGrid(researchType)
      }
    }
  }
}

fun initialSingleGrid(researchType: ResearchType): Grid {
  return when (researchType) {
    ResearchType.CT -> Grid.Single(CutType.CT_AXIAL)
    ResearchType.MG -> Grid.Single(CutType.MG_RCC)
    ResearchType.DX -> Grid.Single(CutType.DX_GENERIC)
  }
}

fun initialTwoVerticalGrid(researchType: ResearchType): Grid {
  return when (researchType) {
    ResearchType.CT -> Grid.TwoVertical(CutType.CT_AXIAL, CutType.CT_FRONTAL)
    ResearchType.MG -> Grid.TwoVertical(CutType.MG_RCC, CutType.MG_LCC)
    ResearchType.DX -> Grid.TwoVertical(CutType.DX_GENERIC, CutType.DX_LEFT_LATERAL)
  }
}

fun initialTwoHorizontalGrid(researchType: ResearchType): Grid {
  return when (researchType) {
    ResearchType.CT -> Grid.TwoHorizontal(CutType.CT_FRONTAL, CutType.CT_SAGITTAL)
    ResearchType.MG -> Grid.TwoHorizontal(CutType.MG_RCC, CutType.MG_LCC)
    ResearchType.DX -> Grid.TwoHorizontal(CutType.DX_GENERIC, CutType.DX_LEFT_LATERAL)
  }
}

fun initialFourGrid(researchType: ResearchType): Grid {
  return when (researchType) {
    ResearchType.CT -> Grid.Four(
      topLeft = CutType.CT_AXIAL,
      topRight = CutType.EMPTY,
      bottomLeft = CutType.CT_FRONTAL,
      bottomRight = CutType.CT_SAGITTAL
    )
    ResearchType.MG -> Grid.Four(
      topLeft = CutType.MG_RCC,
      topRight = CutType.MG_LCC,
      bottomLeft = CutType.MG_RMLO,
      bottomRight = CutType.MG_LMLO
    )
    ResearchType.DX -> Grid.Four(
      topLeft = CutType.DX_GENERIC,
      topRight = CutType.DX_POSTERO_ANTERIOR,
      bottomLeft = CutType.DX_LEFT_LATERAL,
      bottomRight = CutType.DX_RIGHT_LATERAL
    )
  }
}

private val ctCuts = listOf(
  CutType.CT_AXIAL,
  CutType.CT_FRONTAL,
  CutType.CT_SAGITTAL
)

private val mgCuts = listOf(
  CutType.MG_RCC,
  CutType.MG_LCC,
  CutType.MG_LMLO,
  CutType.MG_RMLO
)

private val dxCuts = listOf(
  CutType.DX_GENERIC,
  CutType.DX_POSTERO_ANTERIOR,
  CutType.DX_LEFT_LATERAL,
  CutType.DX_RIGHT_LATERAL
)

fun Grid.buildCuts(data: ResearchSlicesSizesDataNew): List<Cut> =
  when (this) {
    is Grid.Single -> listOf(buildSingleCut(types.first(), data))
    is Grid.TwoVertical -> buildTwoCuts(types, data)
    is Grid.TwoHorizontal -> buildTwoCuts(types, data)
    is Grid.Four -> buildFourCuts(types, data)
  }


private fun buildSingleCut(type: CutType, data: ResearchSlicesSizesDataNew): Cut =
  when (type) {
    CutType.EMPTY -> emptyCut(data)
    CutType.CT_AXIAL -> axialCut(data, ctCuts.filter { it != type })
    CutType.CT_FRONTAL -> frontalCut(data, ctCuts.filter { it != type })
    CutType.CT_SAGITTAL -> sagittalCut(data, ctCuts.filter { it != type })
    CutType.MG_RCC -> mgRcc(data, mgCuts.filter { it != type })
    CutType.MG_LCC -> mgLcc(data, mgCuts.filter { it != type })
    CutType.MG_RMLO -> mgRmlo(data, mgCuts.filter { it != type })
    CutType.MG_LMLO -> mgLmlo(data, mgCuts.filter { it != type })
    CutType.DX_GENERIC -> dxGeneric(data, dxCuts.filter { it != type })
    CutType.DX_POSTERO_ANTERIOR -> dxPosteroAnterior(data, dxCuts.filter { it != type })
    CutType.DX_LEFT_LATERAL -> dxLeftLateral(data, dxCuts.filter { it != type })
    CutType.DX_RIGHT_LATERAL -> dxRightLateral(data, dxCuts.filter { it != type })
  }


private fun buildTwoCuts(types: List<CutType>, data: ResearchSlicesSizesDataNew): List<Cut> {
  val first = types.first()
  val second = types.last()

  val firstResult = cutWithTwoTypes(first, second, data)
  val secondResult = cutWithTwoTypes(second, first, data)

  return listOf(firstResult, secondResult)
}

private fun buildFourCuts(types: List<CutType>, data: ResearchSlicesSizesDataNew): List<Cut> =
  types.map {
    buildEmptySingleCut(it, data)
  }

private fun buildEmptySingleCut(type: CutType, data: ResearchSlicesSizesDataNew): Cut {
  val list = listOf<CutType>()
  return when (type) {
    CutType.EMPTY -> emptyCut(data)
    CutType.CT_AXIAL -> axialCut(data, list)
    CutType.CT_FRONTAL -> frontalCut(data, list)
    CutType.CT_SAGITTAL -> sagittalCut(data, list)
    CutType.MG_RCC -> mgRcc(data, list)
    CutType.MG_LCC -> mgLcc(data, list)
    CutType.MG_RMLO -> mgRmlo(data, list)
    CutType.MG_LMLO -> mgLmlo(data, list)
    CutType.DX_GENERIC -> dxGeneric(data, list)
    CutType.DX_POSTERO_ANTERIOR -> dxPosteroAnterior(data, list)
    CutType.DX_LEFT_LATERAL -> dxLeftLateral(data, list)
    CutType.DX_RIGHT_LATERAL -> dxRightLateral(data, list)
  }
}

private fun cutWithTwoTypes(
  main: CutType,
  second: CutType,
  data: ResearchSlicesSizesDataNew,
): Cut {
  return when (main) {
    CutType.EMPTY -> emptyCut(data)
    CutType.CT_AXIAL -> axialCut(data, ctCuts.filter { it != main && it != second })
    CutType.CT_FRONTAL -> frontalCut(data, ctCuts.filter { it != main && it != second })
    CutType.CT_SAGITTAL -> sagittalCut(data, ctCuts.filter { it != main && it != second })
    CutType.MG_RCC -> mgRcc(data, mgCuts.filter { it != main && it != second })
    CutType.MG_LCC -> mgLcc(data, mgCuts.filter { it != main && it != second })
    CutType.MG_RMLO -> mgRmlo(data, mgCuts.filter { it != main && it != second })
    CutType.MG_LMLO -> mgLmlo(data, mgCuts.filter { it != main && it != second })
    CutType.DX_GENERIC -> dxGeneric(data, dxCuts.filter { it != main && it != second })
    CutType.DX_POSTERO_ANTERIOR -> dxPosteroAnterior(
      data,
      dxCuts.filter { it != main && it != second })
    CutType.DX_LEFT_LATERAL -> dxLeftLateral(data, dxCuts.filter { it != main && it != second })
    CutType.DX_RIGHT_LATERAL -> dxRightLateral(data, dxCuts.filter { it != main && it != second })
  }
}


private fun emptyCut(data: ResearchSlicesSizesDataNew): Cut =
  Cut(
    type = CutType.EMPTY,
    data = ModalityModel(0, 0, 0.0, 0.0, 0, 0, 0),
    color = "",
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun axialCut(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
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
    ),
    researchType = data.type,
    availableCutsForChange = types
  )

private fun frontalCut(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
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
    ),
    researchType = data.type,
    availableCutsForChange = types
  )

private fun sagittalCut(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
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
    ),
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgRcc(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.MG_RCC,
    data = data.modalities[SLICE_TYPE_MG_RCC]
      ?: error("CutsContainerStoreFactory: MG_RCC NOT FOUND IN DATA"),
    color = rcc_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgLcc(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.MG_LCC,
    data = data.modalities[SLICE_TYPE_MG_LCC]
      ?: error("CutsContainerStoreFactory: MG_LCC NOT FOUND IN DATA"),
    color = lcc_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgRmlo(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.MG_RMLO,
    data = data.modalities[SLICE_TYPE_MG_RMLO]
      ?: error("CutsContainerStoreFactory: MG_RMLO NOT FOUND IN DATA"),
    color = rmlo_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgLmlo(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.MG_LMLO,
    data = data.modalities[SLICE_TYPE_MG_LMLO]
      ?: error("CutsContainerStoreFactory: MG_LMLO NOT FOUND IN DATA"),
    color = lmlo_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun dxGeneric(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.DX_GENERIC,
    data = data.modalities[SLICE_TYPE_DX_GENERIC]!!,
    color = generic_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun dxPosteroAnterior(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.DX_POSTERO_ANTERIOR,
    data = data.modalities[SLICE_TYPE_DX_POSTERO_ANTERIOR]!!,
    color = postero_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun dxLeftLateral(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.DX_LEFT_LATERAL,
    data = data.modalities[SLICE_TYPE_DX_LEFT_LATERAL]!!,
    color = left_lateral_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun dxRightLateral(data: ResearchSlicesSizesDataNew, types: List<CutType>): Cut =
  Cut(
    type = CutType.DX_RIGHT_LATERAL,
    data = data.modalities[SLICE_TYPE_DX_RIGHT_LATERAL]!!,
    color = right_lateral_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )






