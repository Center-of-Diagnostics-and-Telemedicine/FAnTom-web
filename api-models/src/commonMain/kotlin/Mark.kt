package model

import kotlinx.serialization.Serializable
import kotlin.math.roundToInt

@Serializable
data class MarkEntity(
  val id: Int,
  val markData: MarkData,
  val type: String,
  val comment: String
) {
  var selected = false
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
  fun name(): String = "x: ${x.roundToInt()}, y: ${y.roundToInt()}, z: ${z.roundToInt()}, r: ${radiusHorizontal.roundToInt()}, shapeType: $shapeType"
}

data class MarkModel(
  val id: Int,
  val markData: MarkData,
  val type: MarkTypeModel?,
  val comment: String
) {
  var selected: Boolean = false
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
  ).also { it.selected = selected }

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
  ).also { it.selected = selected }
