package client.newmvi

import model.CircleShape
import model.SelectedArea
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

object AreaToAxialCircleMapper {

  fun invoke(
    sliceNumber: Int,
    area: SelectedArea,
    coefficient: Double,
    selectedAreaId: Int,
    cutToScreenCoefficient: Double
  ): CircleShape? {
    return if (sliceNumber < (area.z + area.radius) && sliceNumber > (area.z - area.radius)) {
      val x = area.x * cutToScreenCoefficient
      val y = area.y * cutToScreenCoefficient
      val h = abs(sliceNumber - round(area.z)) * coefficient * cutToScreenCoefficient
      val newRadius = sqrt((area.radius * cutToScreenCoefficient).pow(2) - (h).pow(2))
      CircleShape(
        x = x,
        y = y,
        radius = newRadius,
        areaId = area.id,
        highlight = selectedAreaId == area.id
      )
    } else {
      null
    }
  }
}