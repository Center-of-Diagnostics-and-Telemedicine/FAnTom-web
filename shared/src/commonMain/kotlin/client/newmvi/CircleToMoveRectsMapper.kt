package client.newmvi

import model.MoveRectType
import model.CircleShape
import model.MoveRect
import kotlin.math.round

object CircleToMoveRectsMapper {

  fun invoke(
    circle: CircleShape,
    sideLength: Double,
    color: String,
    h: Double
  ): List<MoveRect>? {
    val inCenter: Boolean = round(h) == .0
    return if (inCenter) {
      val left = MoveRect(
        left = circle.x - circle.radius - sideLength / 2,
        top = circle.y - sideLength / 2,
        sideLength = sideLength,
        areaId = circle.areaId,
        color = color,
        type = MoveRectType.LEFT
      )
      val top = MoveRect(
        left = circle.x - sideLength / 2,
        top = circle.y - circle.radius - sideLength / 2,
        sideLength = sideLength,
        areaId = circle.areaId,
        color = color,
        type = MoveRectType.TOP
      )
      val right = MoveRect(
        left = circle.x + circle.radius - sideLength / 2,
        top = circle.y - sideLength / 2,
        sideLength = sideLength,
        areaId = circle.areaId,
        color = color,
        type = MoveRectType.RIGHT
      )
      val bottom = MoveRect(
        left = circle.x - sideLength / 2,
        top = circle.y + circle.radius - sideLength / 2,
        sideLength = sideLength,
        areaId = circle.areaId,
        color = color,
        type = MoveRectType.BOTTOM
      )
      return listOf(left, top, right, bottom)
    } else {
      null
    }
  }
}