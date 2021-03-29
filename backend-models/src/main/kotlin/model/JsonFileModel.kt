package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonFileModel(
  @SerialName("doctors") val doctorComments: List<DoctorCommentModel>? = null,
  @SerialName("taggers") val taggers: Map<String, TaggerModel>? = null,
  val ids: IdsModel? = null,
  val commons: CommonsModel? = null,
  @SerialName("searched_text") val searchedText: List<String>? = null,
  val nodules: List<List<Map<String, NoduleModel?>>>? = null,
  val instances: List<InstanceModel>? = null,
  val dictionary: Map<String, DictionaryEntryModel>? = null
)

@Serializable
data class TaggerModel(
  @SerialName("tagging_system_id") val taggingSystemId: String,
  @SerialName("tagger_id") val taggerId: String
)

@Serializable
data class DictionaryEntryModel(
  @SerialName("RU") val ru: String,
  @SerialName("EN") val en: String
)

@Serializable
data class InstanceModel(
  @SerialName("acquisition_number") val acquisitionNumber: String,
  @SerialName("dcm_filename") val dcmFilename: String,
  @SerialName("instance_number") val instanceNumber: Int,
  val rois: List<RoiModel>,
  @SerialName("series_number") val seriesNumber: Int,
  @SerialName("sop_instance_uid") val sopInstanceUid: String,
)

@Serializable
data class RoiModel(
  @SerialName("anatomical_location") val anatomicalLocation: String,
  @SerialName("confidence") val confidence: Double,
  @SerialName("roi_filename") val roiFilename: String,
  @SerialName("roi_shape") val roiShape: String,
  @SerialName("roi_type") val roiType: String,
  @SerialName("roi_type_index") val roiTypeIndex: Int,
  @SerialName("tagging_system_id") val taggingSystemId: String,
  @SerialName("x_center") val xCenter: Double,
  @SerialName("x_size") val xSize: Double,
  @SerialName("y_center") val yCenter: Double,
  @SerialName("y_size") val ySize: String,
  @SerialName("tagger_id") val taggerId: String,
  val text: String,
)

@Serializable
data class CommonsModel(
  @SerialName("n_dimensions") val nDimensions: Int,
  val diagnosis: String,
  @SerialName("markup_section") val markupSection: String,
  @SerialName("dictionary_section") val dictionarySection: String
)


@Serializable
data class DoctorCommentModel(
  @SerialName("comment") val comment: String,
  @SerialName("id") val id: String
)

@Serializable
data class IdsModel(
  @SerialName("accessionnumber") val accessionNumber: String,
  @SerialName("studyid") val studyId: String,
  @SerialName("study_instance_uid") val studyInstanceUid: String
)

@Serializable
data class NoduleModel(
  @SerialName("diameter(mm)") val diameterMm: Double,
  @SerialName("expertdecision") val expertDecision: List<ExpertDecisionModel>?,
  val type: String,
  val version: Double,
  val x: Double,
  val y: Double,
  val z: Double,
  @SerialName("ztype") val zType: String
)

@Serializable
data class ExpertDecisionModel(
  val comment: String,
  val decision: String,
  val id: String,
  @SerialName("machinelearning") val machineLearning: Boolean,
  @SerialName("propersize") val properSize: Boolean,
  val type: String
)