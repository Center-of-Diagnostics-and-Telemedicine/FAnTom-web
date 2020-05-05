package model

import kotlinx.serialization.Serializable

@Serializable
data class ResearchInitResponse(
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
data class ResearchesResponse(
  val researches: List<Research>
)

@Serializable
data class BaseResponse(
  val status: String,
  val error: String
)

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
  val type: AreaType,
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
data class HounsfieldResponse(
  val huValue: Double
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
  val type: Int,
  val mipMethod: Int,
  val sliceNumber: Int,
  val mipValue: Int
)

@Serializable
data class AuthorizationResponse(val token: String)

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
