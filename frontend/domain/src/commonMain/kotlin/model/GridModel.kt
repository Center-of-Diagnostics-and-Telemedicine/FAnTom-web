package model

import replace

sealed interface GridType {
  object Single : GridType {
    override fun toString(): String {
      return "GridType.Single"
    }
  }

  object TwoVertical : GridType {
    override fun toString(): String {
      return "GridType.TwoVertical"
    }
  }

  object TwoHorizontal : GridType {
    override fun toString(): String {
      return "GridType.TwoHorizontal"
    }
  }

  object Four : GridType {
    override fun toString(): String {
      return "GridType.Four"
    }
  }

  companion object {
    val initial = Four
  }
}

sealed interface MyGrid {
  val types: List<CutType>
}

interface SingleGrid : MyGrid {
  val cut: CutType
}

interface TwoVerticalGrid : MyGrid {
  val top: CutType
  val bottom: CutType
}

interface TwoHorizontalGrid : MyGrid {
  val left: CutType
  val right: CutType
}

interface FourGrid : MyGrid {
  val topLeft: CutType
  val topRight: CutType
  val bottomLeft: CutType
  val bottomRight: CutType
}

sealed class GridModel(override val types: List<CutType>) : MyGrid {
  data class Single(
    override val cut: CutType
  ) : GridModel(listOf(cut)), SingleGrid

  data class TwoVertical(
    override val top: CutType,
    override val bottom: CutType
  ) : GridModel(listOf(top, bottom)), TwoVerticalGrid

  data class TwoHorizontal(
    override val left: CutType,
    override val right: CutType
  ) : GridModel(listOf(left, right)), TwoHorizontalGrid

  data class Four(
    override val topLeft: CutType,
    override val topRight: CutType,
    override val bottomLeft: CutType,
    override val bottomRight: CutType
  ) : GridModel(listOf(topLeft, topRight, bottomLeft, bottomRight)), FourGrid

  companion object {
    fun build(
      type: GridType,
      researchType: ResearchType,
      doseReport: Boolean,
      data: ResearchData
    ): GridModel {
      return when (type) {
        GridType.Single -> initialSingleGrid(researchType)
        GridType.TwoVertical -> initialTwoVerticalGrid(researchType)
        GridType.TwoHorizontal -> initialTwoHorizontalGrid(researchType)
        GridType.Four -> initialFourGrid(researchType, doseReport, data.modalities)
      }
    }
  }
}

fun GridType.buildModel(data: ResearchDataModel): GridModel {
  return when (this) {
    GridType.Single -> initialSingleGrid(data.type)
    GridType.TwoVertical -> initialTwoVerticalGrid(data.type)
    GridType.TwoHorizontal -> initialTwoHorizontalGrid(data.type)
    GridType.Four -> initialFourGrid(data.type)
  }
}

fun initialSingleGrid(researchType: ResearchType): GridModel.Single {
  return when (researchType) {
    ResearchType.CT -> GridModel.Single(CutType.CT_AXIAL)
    ResearchType.MG -> GridModel.Single(CutType.MG_RCC)
    ResearchType.DX -> GridModel.Single(CutType.DX_GENERIC)
  }
}

fun initialTwoVerticalGrid(researchType: ResearchType): GridModel.TwoVertical {
  return when (researchType) {
    ResearchType.CT -> GridModel.TwoVertical(CutType.CT_AXIAL, CutType.CT_FRONTAL)
    ResearchType.MG -> GridModel.TwoVertical(CutType.MG_RCC, CutType.MG_LCC)
    ResearchType.DX -> GridModel.TwoVertical(CutType.DX_GENERIC, CutType.DX_LEFT_LATERAL)
  }
}

fun initialTwoHorizontalGrid(researchType: ResearchType): GridModel.TwoHorizontal {
  return when (researchType) {
    ResearchType.CT -> GridModel.TwoHorizontal(CutType.CT_FRONTAL, CutType.CT_SAGITTAL)
    ResearchType.MG -> GridModel.TwoHorizontal(CutType.MG_RCC, CutType.MG_LCC)
    ResearchType.DX -> GridModel.TwoHorizontal(CutType.DX_GENERIC, CutType.DX_LEFT_LATERAL)
  }
}

fun initialFourGrid(
  researchType: ResearchType,
  doseReport: Boolean,
  modalities: Map<Int, PlaneModel>
): GridModel {
  return when (researchType) {
    ResearchType.CT -> GridModel.Four(
      topLeft = CutType.CT_AXIAL,
      topRight = CutType.EMPTY,
      bottomLeft = CutType.CT_FRONTAL,
      bottomRight = CutType.CT_SAGITTAL
    )
    ResearchType.MG -> {
      if (doseReport) {
        when (modalities.size) {
          1 -> GridModel.Single(CutType.getByValue(modalities.entries.first().key))
          2 -> GridModel.TwoHorizontal(
            left = CutType.getByValue(modalities.entries.first().key),
            right = CutType.getByValue(modalities.entries.last().key)
          )
          3 -> GridModel.Four(
            topLeft = CutType.CT_0,
            topRight = CutType.CT_1,
            bottomLeft = CutType.CT_2,
            bottomRight = CutType.EMPTY
          )
          else -> throw NotImplementedError("initialFourGrid doseReport no variants")
        }
      } else {
        GridModel.Four(
          topLeft = CutType.MG_RCC,
          topRight = CutType.MG_LCC,
          bottomLeft = CutType.MG_RMLO,
          bottomRight = CutType.MG_LMLO
        )
      }
    }
    ResearchType.DX -> GridModel.Four(
      topLeft = CutType.DX_GENERIC,
      topRight = CutType.DX_POSTERO_ANTERIOR,
      bottomLeft = CutType.DX_LEFT_LATERAL,
      bottomRight = CutType.DX_RIGHT_LATERAL
    )
  }
}

fun initialFourGrid(
  researchType: ResearchType,
): GridModel.Four {
  return when (researchType) {
    ResearchType.CT -> GridModel.Four(
      topLeft = CutType.CT_AXIAL,
      topRight = CutType.EMPTY,
      bottomLeft = CutType.CT_FRONTAL,
      bottomRight = CutType.CT_SAGITTAL
    )
    ResearchType.MG -> {
      GridModel.Four(
        topLeft = CutType.MG_RCC,
        topRight = CutType.MG_LCC,
        bottomLeft = CutType.MG_RMLO,
        bottomRight = CutType.MG_LMLO
      )
    }
    ResearchType.DX -> GridModel.Four(
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

private val doseReportCuts = listOf(
  CutType.CT_0,
  CutType.CT_1,
  CutType.CT_2
)

fun GridModel.buildCuts(data: ResearchData): List<Plane> =
  when (this) {
    is GridModel.Single -> listOf(buildSingleCut(types.first(), data))
    is GridModel.TwoVertical,
    is GridModel.TwoHorizontal -> buildTwoCuts(types, data)
    is GridModel.Four -> buildFourCuts(types, data)
  }

fun GridModel.updateCuts(data: ResearchData, oldCut: Plane, newCutType: CutType): List<Plane> {
  val newTypes = types.replace(newValue = newCutType) { it == oldCut.type }
  return when (this) {
    is GridModel.Single -> listOf(buildSingleCut(newCutType, data))
    is GridModel.TwoVertical,
    is GridModel.TwoHorizontal -> buildTwoCuts(newTypes, data)
    is GridModel.Four -> buildFourCuts(newTypes, data)
  }
}


private fun buildSingleCut(type: CutType, data: ResearchData): Plane =
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
    CutType.CT_0 -> ct0(data, doseReportCuts.filter { it != type })
    CutType.CT_1 -> ct1(data, doseReportCuts.filter { it != type })
    CutType.CT_2 -> ct2(data, doseReportCuts.filter { it != type })
  }


private fun buildTwoCuts(types: List<CutType>, data: ResearchData): List<Plane> {
  val first = types.first()
  val second = types.last()

  val firstResult = cutWithTwoTypes(first, second, data)
  val secondResult = cutWithTwoTypes(second, first, data)

  return listOf(firstResult, secondResult)
}

private fun buildFourCuts(types: List<CutType>, data: ResearchData): List<Plane> =
  types.map {
    buildEmptySingleCut(it, data)
  }

private fun buildEmptySingleCut(type: CutType, data: ResearchData): Plane {
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
    CutType.CT_0 -> ct0(data, list)
    CutType.CT_1 -> ct1(data, list)
    CutType.CT_2 -> ct2(data, list)
  }
}

private fun cutWithTwoTypes(
  main: CutType,
  second: CutType,
  data: ResearchData,
): Plane {
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
    CutType.CT_0 -> ct0(data, doseReportCuts.filter { it != main && it != second })
    CutType.CT_1 -> ct1(data, doseReportCuts.filter { it != main && it != second })
    CutType.CT_2 -> ct2(data, doseReportCuts.filter { it != main && it != second })
  }
}


private fun emptyCut(data: ResearchData): Plane =
  Plane(
    type = CutType.EMPTY,
    data = PlaneModel(0, 0, 0.0, 0.0, 0, 0, 0),
    color = "",
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun axialCut(data: ResearchData, types: List<CutType>): Plane =
  Plane(
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

private fun frontalCut(data: ResearchData, types: List<CutType>): Plane =
  Plane(
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

private fun sagittalCut(data: ResearchData, types: List<CutType>): Plane =
  Plane(
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

private fun mgRcc(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_RCC,
    data = data.modalities[SLICE_TYPE_MG_RCC]
      ?: error("CutsContainerStoreFactory: MG_RCC NOT FOUND IN DATA"),
    color = rcc_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgLcc(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_LCC,
    data = data.modalities[SLICE_TYPE_MG_LCC]
      ?: error("CutsContainerStoreFactory: MG_LCC NOT FOUND IN DATA"),
    color = lcc_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgRmlo(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_RMLO,
    data = data.modalities[SLICE_TYPE_MG_RMLO]
      ?: error("CutsContainerStoreFactory: MG_RMLO NOT FOUND IN DATA"),
    color = rmlo_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun mgLmlo(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.MG_LMLO,
    data = data.modalities[SLICE_TYPE_MG_LMLO]
      ?: error("CutsContainerStoreFactory: MG_LMLO NOT FOUND IN DATA"),
    color = lmlo_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun dxGeneric(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_GENERIC,
    data = data.modalities[SLICE_TYPE_DX_GENERIC]!!,
    color = generic_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun dxPosteroAnterior(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_POSTERO_ANTERIOR,
    data = data.modalities[SLICE_TYPE_DX_POSTERO_ANTERIOR]!!,
    color = postero_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun dxLeftLateral(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_LEFT_LATERAL,
    data = data.modalities[SLICE_TYPE_DX_LEFT_LATERAL]!!,
    color = left_lateral_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun dxRightLateral(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.DX_RIGHT_LATERAL,
    data = data.modalities[SLICE_TYPE_DX_RIGHT_LATERAL]!!,
    color = right_lateral_color,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = listOf()
  )

private fun ct0(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_0,
    data = data.modalities[SLICE_TYPE_CT_0]!!,
    color = axialColor,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun ct1(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_1,
    data = data.modalities[SLICE_TYPE_CT_1]!!,
    color = frontalColor,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )

private fun ct2(data: ResearchData, types: List<CutType>): Plane =
  Plane(
    type = CutType.CT_2,
    data = data.modalities[SLICE_TYPE_CT_2]!!,
    color = sagittalColor,
    verticalCutData = null,
    horizontalCutData = null,
    researchType = data.type,
    availableCutsForChange = types
  )






