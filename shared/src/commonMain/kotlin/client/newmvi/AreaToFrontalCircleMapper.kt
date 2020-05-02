package client.newmvi

import model.CircleShape
import model.SelectedArea
import kotlin.math.*

object AreaToFrontalCircleMapper {

  fun invoke(
    sliceNumber: Int,
    area: SelectedArea,
    coefficient: Double,
    selectedAreaId: Int,
    cutToScreenCoefficient: Double,
    height: Int,
    reversed: Boolean
  ): CircleShape? {
    return if ((sliceNumber < (area.y + area.radius)) && (sliceNumber > (area.y - area.radius))) {
      val x = area.x * cutToScreenCoefficient
      val z = if (reversed) height - area.z else area.z
      val y = z * coefficient * cutToScreenCoefficient
      val h = abs(sliceNumber - area.y) * cutToScreenCoefficient
      val newRadius = sqrt((area.radius * cutToScreenCoefficient).pow(2) - h.pow(2))
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