package model

import kotlinx.serialization.Serializable


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
      pixelLength = pixelLength,
      reversed = reversed
    ),
    frontal = SliceSizeData(
      maxFramesSize = frontalReal,
      height = axialInterpolated,
      pixelLength = pixelLength,
      reversed = reversed
    ),
    sagittal = SliceSizeData(
      maxFramesSize = sagittalReal,
      height = axialInterpolated,
      pixelLength = pixelLength,
      reversed = reversed
    ),
    pixelLength = pixelLength,
    reversed = reversed
  )
}

fun ResearchInitModelNew.toResearchSlicesSizesData(): ResearchSlicesSizesData {
  return ResearchSlicesSizesData(
    axial = SliceSizeData(
      maxFramesSize = CT!!.axialTomogram,
      height = CT.frontalScreen,
      pixelLength = CT.pixelLength,
      reversed = CT.reversed
    ),
    frontal = SliceSizeData(
      maxFramesSize = CT.frontalTomogram,
      height = CT.axialScreen,
      pixelLength = CT.pixelLength,
      reversed = CT.reversed
    ),
    sagittal = SliceSizeData(
      maxFramesSize = CT.sagittalTomogram ?: 512,
      height = CT.axialScreen,
      pixelLength = CT.pixelLength,
      reversed = CT.reversed
    ),
    pixelLength = CT.pixelLength,
    reversed = CT.reversed
  )
}

fun initialSlicesSizeData(): SliceSizeData {
  return SliceSizeData(
    maxFramesSize = 0,
    height = 0,
    pixelLength = 0.0,
    reversed = false
  )
}

@Serializable
data class SliceSizeData(
  val maxFramesSize: Int,
  val height: Int,
  val pixelLength: Double,
  val reversed: Boolean
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
data class OK(val status: String = "ok")

@Serializable
data class MarksResponse(
  val marks: List<Mark>
)

@Serializable
data class MarkRequest(
  val mark: Mark,
  val researchId: Int
)

@Serializable
data class NewMarkRequest(
  val mark: AreaToSave,
  val researchId: Int
)

@Serializable
data class Mark(
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
data class AccessionNamesResponse(
  val accessionNames: List<String>
)


@Serializable
data class User(
  val id: Int,
  val name: String,
  val password: String
)
