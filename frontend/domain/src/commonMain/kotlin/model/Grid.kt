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
