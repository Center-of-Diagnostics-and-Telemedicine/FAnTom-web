package model

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlin.math.roundToInt

@Serializable
data class MarkDomain(
  val id: Int,
  val markData: MarkData,
  val type: MarkType,
  val comment: String
) {
  var selected = false
}

@Serializable
data class MarkData(
  val x: Double,
  val y: Double,
  val z: Double,
  val radius: Double,
  val size: Double,
  val cutType: Int
) {
  fun name(): String = "x: ${x.roundToInt()}, y: ${y.roundToInt()}, z: ${z.roundToInt()}, r: ${radius.roundToInt()}"
}

@Serializable
enum class MarkType(val intValue: Int) {
  NO_TYPE_NODULE(-1),
  SOLID_NODULE(0),
  PART_SOLID_NODULE(1),
  PURE_SUBSOLID_NODULE(2),
  NOT_ONKO(3);
}

@Serializer(forClass = MarkType::class)
object MarkTypeSerializer : KSerializer<MarkType> {
  override val descriptor: SerialDescriptor = StringDescriptor
  override fun serialize(encoder: Encoder, value: MarkType) {
    encoder.encodeString(value.toString().toLowerCase())
  }

  override fun deserialize(decoder: Decoder): MarkType {
    return MarkType.valueOf(decoder.decodeString().toUpperCase())
  }
}
