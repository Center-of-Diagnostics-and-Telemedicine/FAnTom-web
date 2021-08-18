package model


sealed interface MyPlane {
  val type: String
  val data: PlaneModel
  val color: String
  val researchType: ResearchType
  val availableCutsForChange: List<String>
}

data class PlanarPlane(
  override val type: String,
  override val data: PlaneModel,
  override val color: String,
  override val researchType: ResearchType,
  override val availableCutsForChange: List<String>
) : MyPlane

data class MultiPlanarPlane(
  override val type: String,
  override val data: PlaneModel,
  override val color: String,
  override val researchType: ResearchType,
  val horizontalPlaneModel: PlaneModel?,
  val verticalPlaneModel: PlaneModel?,
  override val availableCutsForChange: List<String>
) : MyPlane

fun ResearchDataModel.buildPlane(type: String, seriesName: String): MyPlane? =
  when (this.type) {
    ResearchType.CT -> buildMultiPlanarPlane(type, seriesName)
    ResearchType.MG,
    ResearchType.DX -> buildPlanarPlane(type, seriesName)
  }

private fun ResearchDataModel.buildMultiPlanarPlane(type: String, seriesName: String): MyPlane? {
  val seriesModel = series[seriesName]
  val planeModel = seriesModel?.modalityModel?.planes?.get(type) ?: return null
  return MultiPlanarPlane(
    type = type,
    data = planeModel,
    color = getColorByPlaneType(type),
    researchType = this.type,
    availableCutsForChange = seriesModel.modalityModel.planes.keys.filter { it != type },
    horizontalPlaneModel = null,
    verticalPlaneModel = null
  )
}

private fun ResearchDataModel.buildPlanarPlane(type: String, seriesName: String): MyPlane? {
  val seriesModel = series[seriesName]
  val planeModel = seriesModel?.modalityModel?.planes?.get(type) ?: return null
  return PlanarPlane(
    type = type,
    data = planeModel,
    color = getColorByPlaneType(type),
    researchType = this.type,
    availableCutsForChange = seriesModel.modalityModel.planes.keys.filter { it != type }
  )
}

private fun getColorByPlaneType(type: String) =
  when (type) {
    CT_AXIAL_STRING -> yellow
    CT_FRONTAL_STRING -> pink
    CT_SAGITTAL_STRING -> blue
    CT_0_STRING -> yellow
    CT_1_STRING -> pink
    CT_2_STRING -> blue
    MG_RCC_STRING -> yellow
    MG_LCC_STRING -> pink
    MG_RMLO_STRING -> blue
    MG_LMLO_STRING -> green
    DX_GENERIC_STRING -> yellow
    DX_POSTERO_ANTERIOR_STRING -> pink
    DX_LEFT_LATERAL_STRING -> blue
    else -> green
  }