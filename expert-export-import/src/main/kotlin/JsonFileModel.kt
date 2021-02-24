import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JsonFileModel(
  val doctors: List<DoctorModel>,
  val ids: IdsModel,
  val nodules: List<List<Map<String, NoduleModel?>>>?
)

@Serializable
data class DoctorModel(
  @SerialName("comment")
  val comment: String,
  @SerialName("id")
  val id: String
)

@Serializable
data class IdsModel(
  @SerialName("accessionnumber")
  val accessionNumber: String,
  @SerialName("studyid")
  val studyId: String
)

@Serializable
data class NoduleModel(
  @SerialName("diameter(mm)")
  val diameterMm: Double,
  @SerialName("expertdecision")
  val expertDecision: List<ExpertDecisionModel>?,
  val type: String,
  val version: Double,
  val x: Double,
  val y: Double,
  val z: Double,
  @SerialName("ztype")
  val zType: String
)

@Serializable
data class ExpertDecisionModel(
  val comment: String,
  val decision: String,
  val id: String,
  @SerialName("machinelearning")
  val machineLearning: Boolean,
  @SerialName("propersize")
  val properSize: Boolean,
  val type: String
)