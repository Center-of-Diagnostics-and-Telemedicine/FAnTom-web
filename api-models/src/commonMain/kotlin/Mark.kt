package model

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class MarkDomain(
  val id: Int,
  val markData: MarkData,
  val type: MarkType,
  val comment: String
)

@Serializable
data class MarkData(
  val x: Int,
  val y: Int,
  val z: Int,
  val radius: Double,
  val size: Double
) {
  fun name(): String = "x: ${x}, y: ${y}, z: ${z}, r: ${radius.roundToInt()}"
}

enum class MarkType(val intValue: Int) {
  NoTypeNodule(-1),
  SolidNodule(0),
  PartSolidNodule(1),
  PureSubSolidNodule(2),
  NotOnko(3);
}
