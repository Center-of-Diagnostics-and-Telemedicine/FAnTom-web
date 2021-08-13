package model

import kotlinx.serialization.Serializable
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Serializable
data class MarkEntity(
  val id: Int,
  val markData: MarkData,
  val type: String,
  val comment: String
) {
  var selected = false
  var visible = true
}


@Serializable
data class MarkData(
  val x: Double,
  val y: Double,
  val z: Double,
  val radiusHorizontal: Double,
  val radiusVertical: Double,
  val sizeVertical: Double,
  val sizeHorizontal: Double,
  val cutType: Int,
  val shapeType: Int
) {
  fun name(): String =
    "x: ${x.roundToInt()}, y: ${y.roundToInt()}, z: ${z.roundToInt()}, r: ${radiusHorizontal.roundToInt()}, shapeType: $shapeType"
}

data class MarkModel(
  val id: Int,
  val markData: MarkData,
  val type: MarkTypeModel?,
  val comment: String
) {
  var visible: Boolean = true
  var selected: Boolean = false
  var editable: Boolean = true
}

data class MarkTypeModel(
  val typeId: String,
  val en: String,
  val ru: String,
  val color: String
)

fun MarkEntity.toMarkModel(types: Map<String, MarkTypeEntity>): MarkModel =
  MarkModel(
    id = id,
    markData = markData,
    type = types[type]?.toMarkTypeModel(type),
    comment = comment,
  ).also {
    it.selected = selected
    it.visible = visible
  }

fun MarkTypeEntity.toMarkTypeModel(type: String): MarkTypeModel =
  MarkTypeModel(
    typeId = type,
    en = EN ?: "",
    ru = RU ?: "",
    color = CLR ?: ""
  )

fun MarkModel.toMarkEntity(): MarkEntity =
  MarkEntity(
    id = id,
    markData = markData,
    type = type?.typeId ?: "",
    comment = comment,
  ).also {
    it.selected = selected
    it.visible = visible
  }

fun MarkEntity.inBounds(dicomX: Double, dicomY: Double): Boolean {
  return when (markData.shapeType) {
    SHAPE_TYPE_CIRCLE -> inBoundsCircle(dicomX, dicomY)
    SHAPE_TYPE_RECTANGLE -> inBoundsRectangle(dicomX, dicomY)
    SHAPE_TYPE_ELLIPSE -> inBoundsEllipse(dicomX, dicomY)
    else -> throw NotImplementedError("inBounds not implemented for shapeType = ${markData.shapeType}")
  }
}

fun MarkEntity.inBoundsCircle(dicomX: Double, dicomY: Double): Boolean {
  val centerX = markData.x + markData.radiusHorizontal
  val centerY = markData.y + markData.radiusVertical
  return sqrt((centerX - dicomX) + (centerY - dicomY)) <= markData.radiusVertical
}

fun MarkEntity.inBoundsRectangle(dicomX: Double, dicomY: Double): Boolean {
  val bottom = markData.y + markData.radiusVertical * 2
  val top = markData.y
  val right = markData.x + markData.radiusHorizontal * 2
  val left = markData.x
  val inVerticalBound = dicomY in top..bottom
  val inHorizontalBound = dicomX in left..right
  return inVerticalBound && inHorizontalBound
}

fun MarkEntity.inBoundsEllipse(dicomX: Double, dicomY: Double): Boolean {
  val a = markData.radiusHorizontal
  val b = markData.radiusVertical
  val h = markData.x + markData.radiusHorizontal
  val k = markData.y + markData.radiusVertical
  return (dicomX - h).pow(2) / a.pow(2) + (dicomY - k).pow(2) / b.pow(2) <= 1
}
