package remote.mappers

import debugLog
import model.MarkTypeEntity
import model.ModalityModel
import model.PlaneModel
import model.ResearchInitModel
import model.fantom.*
import java.awt.Color

fun FantomResearchInitModel.toResponse(dictionary: List<List<Map<String, FantomMarkTypeEntity>>>?): ResearchInitModel {
  debugLog("ResearchInitResponse income")
  val resultMarkTypes = mutableMapOf<String, MarkTypeEntity>()

  dictionary?.first()?.map { maps ->
    maps.map { entry ->
      resultMarkTypes[entry.key] = transformMarkEntity(entry.value)
    }
  }
  return ResearchInitModel(
    CT = CT.toModalityModel(),
    MG = MG.toModalityModel(),
    DX = DX.toModalityModel(),
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

fun FantomCTInitModel?.toModalityModel(): ModalityModel? {
  if (this == null) return null
  val planes = mutableMapOf<String, PlaneModel>()
  this.CT0?.let { planes["CT0"] = it.toPlaneModel() }
  this.CT1?.let { planes["CT1"] = it.toPlaneModel() }
  this.CT2?.let { planes["CT2"] = it.toPlaneModel() }
  this.ct_axial?.let { planes["ct_axial"] = it.toPlaneModel() }
  this.ct_frontal?.let { planes["ct_frontal"] = it.toPlaneModel() }
  this.ct_sagittal?.let { planes["ct_sagittal"] = it.toPlaneModel() }
  return ModalityModel(
    planes = planes,
    reversed = reversed
  )
}

fun FantomMGInitModel?.toModalityModel(): ModalityModel? {
  if (this == null) return null
  val planes = mutableMapOf<String, PlaneModel>()
  this.mg_lcc.let { planes["mg_lcc"] = it.toPlaneModel() }
  this.mg_lmlo.let { planes["mg_lmlo"] = it.toPlaneModel() }
  this.mg_rcc.let { planes["mg_rcc"] = it.toPlaneModel() }
  this.mg_rmlo.let { planes["mg_rmlo"] = it.toPlaneModel() }

  return ModalityModel(
    planes = planes,
    reversed = reversed
  )
}

fun FantomDXInitModel?.toModalityModel(): ModalityModel? {
  if (this == null) return null
  val planes = mutableMapOf<String, PlaneModel>()
  this.dx0.let { planes["dx0"] = it.toPlaneModel() }

  return ModalityModel(
    planes = planes,
    reversed = reversed
  )
}

private fun FantomPlaneModel.toPlaneModel(): PlaneModel {
  return PlaneModel(
    dicomSizeH = dicom_size_h,
    dicomSizeV = dicom_size_v,
    dicomStepH = dicom_step_h,
    dicomStepV = dicom_step_v,
    nImages = n_images,
    screenSizeH = screen_size_h,
    screenSizeV = screen_size_v,
    reversed = reversed,
    SOPInstanceUID = SOPInstanceUID,
    file = file
  )
}

