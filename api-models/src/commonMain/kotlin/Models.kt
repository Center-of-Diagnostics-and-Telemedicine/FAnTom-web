package model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorModel(val error: Int, val message: String = "")

@Serializable
data class AuthorizationResponse(
  val response: AuthorizationModel? = null,
  val error: ErrorModel? = null
)

@Serializable
data class AuthorizationModel(val token: String)

@Serializable
data class ResearchInitResponse(
  val response: ResearchInitModel? = null,
  val error: ErrorModel? = null
)


@Serializable
data class ResearchSlicesSizesData(
  val axial: SliceSizeData,
  val frontal: SliceSizeData,
  val sagittal: SliceSizeData,
  val pixelLength: Double,
  val researchId: Int = -1,
  val reversed: Boolean = false
)

fun initialResearchSlicesSizesData(): ResearchSlicesSizesData {
  return ResearchSlicesSizesData(
    axial = initialSlicesSizeData(),
    frontal = initialSlicesSizeData(),
    sagittal = initialSlicesSizeData(),
    pixelLength = .0
  )
}

fun ResearchInitModel.toResearchSlicesSizesData(): ResearchSlicesSizesData {
  return ResearchSlicesSizesData(
    axial = SliceSizeData(
      maxFramesSize = axialReal,
      height = frontalInterpolated,
      pixelLength = pixelLength
    ),
    frontal = SliceSizeData(
      maxFramesSize = frontalReal,
      height = axialInterpolated,
      pixelLength = pixelLength
    ),
    sagittal = SliceSizeData(
      maxFramesSize = sagittalReal,
      height = axialInterpolated,
      pixelLength = pixelLength
    ),
    pixelLength = pixelLength,
    reversed = reversed
  )
}

fun initialSlicesSizeData(): SliceSizeData {
  return SliceSizeData(
    maxFramesSize = 0,
    height = 0,
    pixelLength = 0.0
  )
}

@Serializable
data class SliceSizeData(
  val maxFramesSize: Int,
  val height: Int,
  val pixelLength: Double
)

@Serializable
data class Research(
  val id: Int,
  val name: String,
  val seen: Boolean,
  val done: Boolean,
  val marked: Boolean
)


@Serializable
data class ResearchInitModel(
  val axialReal: Int,
  val axialInterpolated: Int,
  val frontalReal: Int,
  val frontalInterpolated: Int,
  val sagittalReal: Int,
  val sagittalInterpolated: Int,
  val pixelLength: Double,
  val reversed: Boolean
)

@Serializable
data class SliceResponse(
  val response: SliceModel? = null,
  val error: ErrorModel? = null
)

@Serializable
data class SliceModel(val image: ByteArray) {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as SliceModel

    if (!image.contentEquals(other.image)) return false

    return true
  }

  override fun hashCode(): Int {
    return image.contentHashCode()
  }

}

@Serializable
data class HounsfieldResponse(
  val response: HounsfieldModel? = null,
  val error: ErrorModel? = null
)

@Serializable
data class HounsfieldModel(val huValue: Double)


@Serializable
data class ResearchesResponse(
  val response: ResearchesModel? = null,
  val error: ErrorModel? = null
)

@Serializable
data class ResearchesModel(val researches: List<Research>)

@Serializable
data class BaseResponse(
  val response: OK? = null,
  val error: ErrorModel? = null
)

@Serializable
data class OK(val status: String = "ok")

@Serializable
data class MarksResponse(
  val marks: List<SelectedArea>
)

@Serializable
data class MarkRequest(
  val mark: SelectedArea,
  val researchId: Int
)

@Serializable
data class NewMarkRequest(
  val mark: AreaToSave,
  val researchId: Int
)

@Serializable
data class SelectedArea(
  val x: Double,
  val y: Double,
  val z: Double,
  val areaType: AreaType,
  val radius: Double,
  val size: Double,
  val id: Int,
  val comment: String
)

@Serializable
data class AreaToSave(
  val x: Double,
  val y: Double,
  val z: Double,
  val radius: Double,
  val size: Double
)

@Serializable
data class HounsfieldRequest(
  val axialCoord: Int,
  val frontalCoord: Int,
  val sagittalCoord: Int
)

@Serializable
data class AccessionNamesResponse(
  val accessionNames: List<String>
)

@Serializable
data class SliceRequest(
  val black: Double,
  val white: Double,
  val gamma: Double,
  val sliceType: Int,
  val mipMethod: Int,
  val sliceNumber: Int,
  val mipValue: Int
)

@Serializable
data class AuthorizationRequest(val name: String, val password: String)

@Serializable
data class RegistrationRequest(val name: String, val password: String, val role: Int)

@Serializable
data class User(
  val id: Int,
  val name: String,
  val password: String
)

@Serializable
data class ConfirmCTTypeRequest(
  val researchId: Int,
  val ctType: Int,
  val leftPercent: Int,
  val rightPercent: Int
)
