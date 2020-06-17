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

@Serializable
sealed class MarkType(val intValue: Int) {
  @Serializable
  object NoTypeNodule : MarkType(-1)

  @Serializable
  object SolidNodule : MarkType(0)

  @Serializable
  object PartSolidNodule : MarkType(1)

  @Serializable
  object PureSubSolidNodule : MarkType(2)

  @Serializable
  object NotOnko : MarkType(3)

  companion object {
    fun build(intValue: Int): MarkType {
      return when (intValue) {
        0 -> SolidNodule
        1 -> PartSolidNodule
        2 -> PureSubSolidNodule
        3 -> NotOnko
        else -> NoTypeNodule
      }
    }
  }
}
