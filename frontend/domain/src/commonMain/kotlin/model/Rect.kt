package model


data class Rect(
  val left: Double,
  val top: Double,
  val sideLength: Double,
  val markId: Int,
  val type: MoveRectType
)

fun Shape.toRects(cut: Plane): List<Rect> {
  val sideLength = cut.data.screenSizeH.toDouble() / 100
  return when (cut.researchType) {
    ResearchType.CT -> {
      mainRects(sideLength)
    }
    ResearchType.DX,
    ResearchType.MG -> {
      val leftTop = Rect(
        left = dicomCenterX - dicomRadiusHorizontal,
        top = dicomCenterY - dicomRadiusVertical,
        sideLength = sideLength,
        markId = id,
        type = MoveRectType.LEFT_TOP
      )
      val rightTop = Rect(
        left = dicomCenterX + dicomRadiusHorizontal,
        top = dicomCenterY - dicomRadiusVertical,
        sideLength = sideLength,
        markId = id,
        type = MoveRectType.RIGHT_TOP
      )
      val leftBottom = Rect(
        left = dicomCenterX - dicomRadiusHorizontal,
        top = dicomCenterY + dicomRadiusVertical,
        sideLength = sideLength,
        markId = id,
        type = MoveRectType.LEFT_BOTTOM
      )
      val rightBottom = Rect(
        left = dicomCenterX + dicomRadiusHorizontal,
        top = dicomCenterY + dicomRadiusVertical,
        sideLength = sideLength,
        markId = id,
        type = MoveRectType.RIGHT_BOTTOM
      )
      val list = mainRects(sideLength).toMutableList()
      list.addAll(listOf(leftTop, rightTop, leftBottom, rightBottom))
      return list
    }
  }
}

private fun Shape.mainRects(sideLength: Double): List<Rect> {
  val left = Rect(
    left = dicomCenterX - dicomRadiusHorizontal,
    top = dicomCenterY,
    sideLength = sideLength,
    markId = id,
    type = MoveRectType.LEFT
  )
  val top = Rect(
    left = dicomCenterX,
    top = dicomCenterY - dicomRadiusVertical,
    sideLength = sideLength,
    markId = id,
    type = MoveRectType.TOP
  )
  val right = Rect(
    left = dicomCenterX + dicomRadiusHorizontal,
    top = dicomCenterY,
    sideLength = sideLength,
    markId = id,
    type = MoveRectType.RIGHT
  )
  val bottom = Rect(
    left = dicomCenterX,
    top = dicomCenterY + dicomRadiusVertical,
    sideLength = sideLength,
    markId = id,
    type = MoveRectType.BOTTOM
  )
  return listOf(left, top, right, bottom)
}