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
    fun build(type: GridType): Grid {
      return when (type) {
        GridType.Single -> initialSingleGrid()
        GridType.TwoVertical -> initialTwoVerticalGrid()
        GridType.TwoHorizontal -> initialTwoHorizontalGrid()
        GridType.Four -> initialFourGrid()
      }
    }
  }
}

fun initialSingleGrid(): Grid {
  return Grid.Single(CutType.Axial)
}

fun initialTwoVerticalGrid(): Grid {
  return Grid.TwoVertical(CutType.Axial, CutType.Frontal)
}

fun initialTwoHorizontalGrid(): Grid {
  return Grid.TwoHorizontal(CutType.Frontal, CutType.Sagittal)
}

fun initialFourGrid(): Grid {
  return Grid.Four(
    topLeft = CutType.Axial,
    topRight = CutType.Empty,
    bottomLeft = CutType.Frontal,
    bottomRight = CutType.Sagittal
  )
}
