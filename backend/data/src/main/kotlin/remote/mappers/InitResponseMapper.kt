package remote.mappers

import debugLog
import model.MarkTypeEntity
import model.PlaneModel
import model.fantom.FantomMarkTypeEntity
import model.fantom.FantomModalityInitModel
import model.fantom.FantomPlaneModel
import model.fantom.FantomResearchInitModel
import model.init.ModalityModel
import model.init.ResearchInitModel
import java.awt.Color

fun FantomResearchInitModel.toResponse(): ResearchInitModel {
  debugLog("ResearchInitResponse income")
  val resultMarkTypes = mutableMapOf<String, MarkTypeEntity>()

  dictionary?.firstOrNull()?.map { entry ->
    resultMarkTypes[entry.key] = transformMarkEntity(entry.value)
  }
  return ResearchInitModel(
    CT = CT?.toModalityModel(),
    MG = MG?.toModalityModel(),
    DX = DX?.toModalityModel(),
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

fun FantomModalityInitModel?.toModalityModel(): ModalityModel? {
  if (this == null) return null
  val planes = mapPlanes()
  return ModalityModel(
    planes = planes,
    reversed = reversed
  )

}

private fun FantomModalityInitModel.mapPlanes(): MutableMap<String, PlaneModel> {
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

