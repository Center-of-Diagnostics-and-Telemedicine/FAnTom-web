package remote.mappers

import debugLog
import model.*
import model.fantom.*
import model.init.ModalityModel
import model.init.ResearchInitModel
import model.init.SeriesModel
import java.awt.Color

fun FantomResearchInitModel.toResponse(): ResearchInitModel {
  debugLog("ResearchInitResponse income")
  val resultMarkTypes = mutableMapOf<String, MarkTypeEntity>()

  dictionary?.firstOrNull()?.map { entry ->
    resultMarkTypes[entry.key] = transformMarkEntity(entry.value)
  }
  return ResearchInitModel(
    CT = CT?.toSeriesModel(),
    MG = MG?.toSeriesModel(),
    DX = DX?.toSeriesModel(),
    dictionary = resultMarkTypes
  )
}

private fun transformMarkEntity(
  value: FantomMarkTypeEntity
): MarkTypeEntity {
  val rgb = value.CLR?.replace("\\s".toRegex(), "")?.split(",")
  return if (rgb != null && rgb.size > 1) {
    val red = rgb[0]
    val green = rgb[1]
    val blue = rgb[2]
    if (red.isEmpty().not() && green.isEmpty().not() && blue.isEmpty().not()) {
      val color = Color(red.toInt(), green.toInt(), blue.toInt())
      val hex = "#" + Integer.toHexString(color.rgb).substring(2)
      MarkTypeEntity(CLR = hex, EN = value.EN, RU = value.RU)
    } else MarkTypeEntity(CLR = value.CLR, EN = value.EN, RU = value.RU)
  } else MarkTypeEntity(CLR = value.CLR, EN = value.EN, RU = value.RU)
}

fun FantomModalityInitModel?.toSeriesModel(): Map<String, SeriesModel>? {
  if (this == null) return null
  return mapSeries(mapPlanes())
}

private fun FantomModalityInitModel.mapPlanes(): Map<String, PlaneModel> {
  val planes = mutableMapOf<String, PlaneModel>()
  dimensions.map {
    val sopInstanceUid = it.key
    val fantomPlaneModel = it.value
    val type = fantomPlaneModel.type
    fantomPlaneModel
      .toPlaneModel(reversed, sopInstanceUid)
      .let { planeModel ->
        val key = if (type.isNullOrEmpty()) sopInstanceUid else type
        planes[key] = planeModel
      }
  }
  return planes
}

private fun FantomPlaneModel.toPlaneModel(reversed: Boolean, sopInstanceUid: String?): PlaneModel {
  return PlaneModel(
    dicomSizeH = dicom_size_h,
    dicomSizeV = dicom_size_v,
    dicomStepH = dicom_step_h,
    dicomStepV = dicom_step_v,
    nImages = n_images,
    screenSizeH = screen_size_h,
    screenSizeV = screen_size_v,
    reversed = reversed,
    SOPInstanceUID = sopInstanceUid,
    seriesInstanceUid = series_instance_uid
  )
}

private fun FantomModalityInitModel.mapSeries(planes: Map<String, PlaneModel>): Map<String, SeriesModel> {
  val defaultSeries = mapDefaultSeries(planes)
  val otherSeries = mapOtherSeries(planes)
  val result = mutableMapOf<String, SeriesModel>()
  result[defaultSeriesName()] = defaultSeries
  otherSeries.forEach { result[it.name] = it }
  return result
}

fun FantomModalityInitModel.mapDefaultSeries(planes: Map<String, PlaneModel>): SeriesModel {
  val defaultPlanes = mutableMapOf<String, PlaneModel>()
  defaultStringTypes().forEach { defaultType ->
    planes[defaultType]?.let { plane ->
      defaultPlanes[defaultType] = plane
    }
  }
  return SeriesModel(
    name = defaultSeriesName(),
    modalityModel = ModalityModel(planes = defaultPlanes, reversed = this.reversed)
  )
}

fun FantomModalityInitModel.mapOtherSeries(planes: Map<String, PlaneModel>): List<SeriesModel> {
  val otherPlanes = planes.filterKeys { defaultStringTypes().contains(it).not() }
  return otherPlanes.map {
    SeriesModel(
      name = it.key,
      modalityModel = ModalityModel(planes = mapOf(it.key to it.value), reversed = this.reversed)
    )
  }
}

fun FantomModalityInitModel.defaultStringTypes(): List<String> =
  when (this) {
    is FantomCTInitModel -> ctDefaultStringTypes
    is FantomDXInitModel -> dxDefaultStringTypes
    is FantomMGInitModel -> mgDefaultStringTypes
  }

fun FantomModalityInitModel.defaultSeriesName(): String =
  when (this) {
    is FantomCTInitModel -> CT_DEFAULT_SERIES_STRING
    is FantomDXInitModel -> DX_DEFAULT_SERIES_STRING
    is FantomMGInitModel -> MG_DEFAULT_SERIES_STRING
  }

