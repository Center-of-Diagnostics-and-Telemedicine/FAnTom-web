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
  val size: Double,
  val cutType: Int
) {
  fun name(): String = "x: ${x.roundToInt()}, y: ${y.roundToInt()}, z: ${z.roundToInt()}, r: ${radiusHorizontal.roundToInt()}"
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
  val ru: String
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
    ru = RU ?: ""
  )

fun MarkModel.toMarkEntity(): MarkEntity =
  MarkEntity(
    id = id,
    markData = markData,
    type = type?.typeId ?: "",
    comment = comment,
  ).also { it.selected = selected }

//@Serializable
//enum class MarkType(val intValue: Int) {
//  NO_TYPE_NODULE(-1),
//  SOLID_NODULE(0),
//  PART_SOLID_NODULE(1),
//  PURE_SUBSOLID_NODULE(2),
//  NOT_ONKO(3);
//}

//@Serializer(forClass = MarkType::class)
//object MarkTypeSerializer : KSerializer<MarkType> {
//  override val descriptor: SerialDescriptor = StringDescriptor
//  override fun serialize(encoder: Encoder, value: MarkType) {
//    encoder.encodeString(value.toString().toLowerCase())
//  }
//
//  override fun deserialize(decoder: Decoder): MarkType {
//    return MarkType.valueOf(decoder.decodeString().toUpperCase())
//  }
//}
