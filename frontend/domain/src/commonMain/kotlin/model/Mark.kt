package model

data class Mark(
  val id: Int,
  val markData: MarkData,
  val size: Double,
  val type: MarkType,
  val comment: String
)

data class MarkToSave(
  val markData: MarkData
)

data class MarkData(
  val x: Double,
  val y: Double,
  val z: Double,
  val radius: Double
)

sealed class MarkType {
  object NoTypeNodule : MarkType()
  object SolidNodule : MarkType()
  object PartSolidNodule : MarkType()
  object PureSubSolidNodule : MarkType()
  object NotOnko : MarkType()
}
