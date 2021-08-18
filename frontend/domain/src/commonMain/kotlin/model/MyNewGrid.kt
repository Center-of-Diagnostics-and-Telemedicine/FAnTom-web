package model

sealed interface MyNewGrid {
  interface EmptyGrid : MyNewGrid

  interface SingleGrid : MyNewGrid {
    val plane: MyPlane
  }

  interface TwoVerticalGrid : MyNewGrid {
    val top: MyPlane
    val bottom: MyPlane
  }

  interface TwoHorizontalGrid : MyNewGrid {
    val left: MyPlane
    val right: MyPlane
  }

  interface ThreeGrid : MyNewGrid {
    val topLeft: MyPlane
    val bottomLeft: MyPlane
    val bottomRight: MyPlane
  }

  interface FourGrid : MyNewGrid {
    val topLeft: MyPlane
    val topRight: MyPlane
    val bottomLeft: MyPlane
    val bottomRight: MyPlane
  }
}

object EmptyGridModel : MyNewGrid.EmptyGrid {
  override fun toString(): String = "EmptyGridModel"
}

data class SingleGridModel(
  override val plane: MyPlane
) : MyNewGrid.SingleGrid {
  override fun toString(): String = "SingleGridModel, plane = $plane"
}

data class TwoVerticalGridModel(
  override val top: MyPlane,
  override val bottom: MyPlane
) : MyNewGrid.TwoVerticalGrid {
  override fun toString(): String = "TwoVerticalGridModel, top = $top, bottom = $bottom"
}

data class TwoHorizontalGridModel(
  override val left: MyPlane,
  override val right: MyPlane
) : MyNewGrid.TwoHorizontalGrid {
  override fun toString(): String = "TwoHorizontalGridModel, left = $left, right = $right"
}

data class ThreeGridModel(
  override val topLeft: MyPlane,
  override val bottomLeft: MyPlane,
  override val bottomRight: MyPlane,
) : MyNewGrid.ThreeGrid {
  override fun toString(): String = "ThreeGridModel, topLeft = $topLeft, bottomLeft = $bottomLeft, bottomRight = $bottomRight"
}

data class FourGridModel(
  override val topLeft: MyPlane,
  override val topRight: MyPlane,
  override val bottomLeft: MyPlane,
  override val bottomRight: MyPlane
) : MyNewGrid.FourGrid {
  override fun toString(): String = "FourGridModel, topLeft = $topLeft, topRight = $topRight, bottomLeft = $bottomLeft, bottomRight = $bottomRight"
}

sealed interface MyNewGridType {
  object Single : MyNewGridType {
    override fun toString(): String = "SingleGridType"
  }

  object TwoVertical : MyNewGridType {
    override fun toString(): String = "TwoVertical"
  }

  object TwoHorizontal : MyNewGridType {
    override fun toString(): String = "TwoHorizontal"
  }

  object Three : MyNewGridType {
    override fun toString(): String = "Three"
  }

  object Four : MyNewGridType {
    override fun toString(): String = "Four"
  }

  object None : MyNewGridType {
    override fun toString(): String = "None"
  }
}

fun MyNewGrid.toGridType(): MyNewGridType =
  when (this) {
    is MyNewGrid.SingleGrid -> MyNewGridType.Single
    is MyNewGrid.TwoVerticalGrid -> MyNewGridType.TwoVertical
    is MyNewGrid.TwoHorizontalGrid -> MyNewGridType.TwoHorizontal
    is MyNewGrid.ThreeGrid -> MyNewGridType.Three
    is MyNewGrid.FourGrid -> MyNewGridType.Four
    is MyNewGrid.EmptyGrid -> MyNewGridType.None
  }

fun ResearchDataModel.buildDefaultGrid(): MyNewGrid =
  when (type) {
    ResearchType.CT -> buildGrid(
      series[CT_DEFAULT_SERIES_STRING]!!.modalityModel.planes,
      CT_DEFAULT_SERIES_STRING
    )
    ResearchType.MG -> buildGrid(
      series[MG_DEFAULT_SERIES_STRING]!!.modalityModel.planes,
      MG_DEFAULT_SERIES_STRING
    )
    ResearchType.DX -> buildGrid(
      series[DX_DEFAULT_SERIES_STRING]!!.modalityModel.planes,
      DX_DEFAULT_SERIES_STRING
    )
  }

fun ResearchDataModel.buildGrid(seriesName: String): MyNewGrid {
  return buildGrid(series[seriesName]!!.modalityModel.planes, seriesName)
}

fun ResearchDataModel.buildGrid(gridType: MyNewGridType, seriesName: String): MyNewGrid {
  return buildGrid(gridType, series[seriesName]!!.modalityModel.planes, seriesName)
}

private fun ResearchDataModel.buildGrid(
  gridType: MyNewGridType,
  planes: Map<String, PlaneModel>,
  seriesName: String
): MyNewGrid {
  val map = planes.mapNotNull { buildPlane(type = it.key, seriesName = seriesName) }
  return when (gridType) {
    MyNewGridType.Single -> SingleGridModel(plane = map.first())
    MyNewGridType.TwoVertical -> TwoVerticalGridModel(top = map.first(), bottom = map.last())
    MyNewGridType.TwoHorizontal -> TwoHorizontalGridModel(left = map.first(), right = map.last())
    MyNewGridType.Three -> ThreeGridModel(
      topLeft = map.first(),
      bottomLeft = map[1],
      bottomRight = map.last()
    )
    MyNewGridType.Four -> FourGridModel(
      topLeft = map.first(),
      topRight = map[1],
      bottomLeft = map[2],
      bottomRight = map.last()
    )
    MyNewGridType.None -> EmptyGridModel
  }
}

private fun ResearchDataModel.buildGrid(
  planes: Map<String, PlaneModel>,
  seriesName: String
): MyNewGrid {
  val map = planes.mapNotNull { buildPlane(type = it.key, seriesName = seriesName) }
  return when (map.size) {
    1 -> SingleGridModel(plane = map.first())
    2 -> TwoVerticalGridModel(top = map.first(), bottom = map.last())
    3 -> ThreeGridModel(topLeft = map.first(), bottomLeft = map[1], bottomRight = map.last())
    4 -> FourGridModel(
      topLeft = map.first(),
      topRight = map[1],
      bottomLeft = map[2],
      bottomRight = map.last()
    )
    else -> throw NotImplementedError("buildGrid not implemented for map = $map")
  }
}