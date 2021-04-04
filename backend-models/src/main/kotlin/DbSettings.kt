import model.ID_FIELD
import model.SHAPE_TYPE_CIRCLE
import model.SLICE_TYPE_CT_AXIAL
import model.UserRole
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserVos : Table(name = USER_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val name: Column<String> = varchar(name = NAME_FIELD, length = 100).uniqueIndex()
  val password: Column<String> = varchar(name = PASSWORD_FIELD, length = 200)
  val role: Column<Int> = integer(name = ROLE_FIELD).default(UserRole.TAGGER.value)
  override val primaryKey = PrimaryKey(id, name = "UserPKConstraintName")
}

object ResearchVos : Table(name = RESEARCH_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val accessionNumber: Column<String> = varchar(name = ACCESSION_NUMBER_FIELD, length = 200)
  val studyInstanceUID: Column<String> = varchar(name = STUDY_INSTANCE_UID_FIELD, length = 200)
  val studyID: Column<String> = varchar(name = STUDY_ID_FIELD, length = 200)
  val protocol: Column<String> = varchar(name = PROTOCOL_FIELD, length = 200)
  val accessionNumber_base: Column<String> = varchar(
    name = ACCESSION_NUMBER_BASE_FIELD,
    length = 200
  )
  val accessionNumber_lastDigit: Column<String> = varchar(
    name = ACCESSION_NUMBER_LAST_DIGIT_FIELD,
    length = 200
  )
  val json_name: Column<String> = varchar(name = JSON_NAME_FIELD, length = 200)
  val doctor1: Column<String> = varchar(name = DOCTOR_1_FIELD, length = 200)
  val doctor2: Column<String> = varchar(name = DOCTOR_2_FIELD, length = 200)
  val pos_in_block: Column<String> = varchar(name = POS_IN_BLOCK_FIELD, length = 200)
  val modality: Column<String> = varchar(name = MODALITY_FIELD, length = 200)
  val category: Column<String> = varchar(name = CATEGORY_FIELD, length = 200)
  override val primaryKey = PrimaryKey(id, name = "ResearchPKConstraintName")
}

object UserResearchVos : Table("user_research") {
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer("research_$ID_FIELD")
  val seen: Column<Int> = integer(name = SEEN_FIELD).default(0)
  val done: Column<Int> = integer(name = DONE_FIELD).default(0)
  override val primaryKey = PrimaryKey(userId, researchId, name = "UserResearchPKConstraintName")
}

object CovidMarksVos : Table(COVID_MARKS_TABLE) {
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer("research_$ID_FIELD")
  val rightUpperLobeValue: Column<Int> = integer(RIGHT_UPPER_LOBE_VALUE_FIELD)
  val middleLobeValue: Column<Int> = integer(MIDDLE_LOBE_VALUE_FIELD)
  val rightLowerLobeValue: Column<Int> = integer(RIGHT_LOWER_LOBE_VALUE_FIELD)
  val leftUpperLobeValue: Column<Int> = integer(LEFT_UPPER_LOBE_VALUE_FIELD)
  val leftLowerLobeValue: Column<Int> = integer(LEFT_LOWER_LOBE_VALUE_FIELD)
  override val primaryKey = PrimaryKey(userId, researchId, name = "CovidMarkPKConstraintName")
}

object MultiPlanarMarksVos : Table(MULTI_PLANAR_MARKS_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer("research_$ID_FIELD")
  val x: Column<Double> = double(X_FIELD)
  val y: Column<Double> = double(Y_FIELD)
  val z: Column<Double> = double(Z_FIELD)
  val radius: Column<Double> = double(RADIUS_HORIZONTAL_FIELD)
  val size: Column<Double> = double(SIZE_VERTICAL_FIELD)
  val type: Column<String> = varchar(name = MARK_TYPE_FIELD, length = 20).default("")
  val comment: Column<String> = varchar(name = COMMENT_FILED, length = 200).default("")
  val cutType: Column<Int> = integer(name = CUT_TYPE_FILED).default(SLICE_TYPE_CT_AXIAL)
  override val primaryKey = PrimaryKey(id, name = "MarkPKConstraintName")
}

object PlanarMarksVos : Table(PLANAR_MARKS_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer("research_$ID_FIELD")
  val x: Column<Double> = double(X_FIELD)
  val y: Column<Double> = double(Y_FIELD)
  val radiusHorizontal: Column<Double> = double(RADIUS_HORIZONTAL_FIELD)
  val radiusVertical: Column<Double> = double(RADIUS_VERTICAL_FIELD)
  val sizeVertical: Column<Double> = double(SIZE_VERTICAL_FIELD)
  val sizeHorizontal: Column<Double> = double(SIZE_HORIZONTAL_FIELD)
  val type: Column<String> = varchar(name = MARK_TYPE_FIELD, length = 20).default("")
  val comment: Column<String> = varchar(name = COMMENT_FILED, length = 200).default("")
  val cutType: Column<Int> = integer(name = CUT_TYPE_FILED)
  val shapeType: Column<Int> = integer(name = SHAPE_TYPE_FILED).default(SHAPE_TYPE_CIRCLE)
  override val primaryKey = PrimaryKey(id, name = "MarkPKConstraintName")
}

object ExpertMarksVos : Table(EXPERT_MARKS_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer(name = RESEARCH_ID_FIELD)
  val roiId: Column<Int?> = integer("roi_$ID_FIELD").nullable()
  val xCenter: Column<Double> = double(X_FIELD)
  val yCenter: Column<Double> = double(Y_FIELD)
  val xSize: Column<Double> = double("x_size")
  val ySize: Column<Double> = double("y_size")
  val acquisitionNumber: Column<String> = varchar("acquisition_number", 100)
  val dcmFilename: Column<String> = varchar("dcm_filename", 200)
  val instanceNumber: Column<Int> = integer("instance_number")
  val seriesNumber: Column<Int> = integer("series_number")
  val sopInstanceUid: Column<String> = varchar("sop_instance_uid", 200)
  val anatomicalLocation: Column<String> = varchar("anatomical_location", 200)
  val confidence: Column<Double> = double("confidence")
  val roiFilename: Column<String> = varchar("roi_filename", 200)
  val roiShape: Column<String> = varchar("roi_shape", 100)
  val roiType: Column<String> = varchar("roi_type", 100)
  val roiTypeIndex: Column<Int> = integer("roi_type_index")
  val taggerId: Column<String> = varchar("tagger_id", 100)
  val text: Column<String> = varchar("text", 200)
  val confirmed: Column<Boolean?> = bool("confirmed").nullable()

  override val primaryKey = PrimaryKey(id, name = "ExportMarkPKConstraintName")
}

object UserExpertMarkVos : Table("user_expert_mark") {
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer("research_$ID_FIELD")
  val expertMarkId: Column<Int> = integer("expert_mark_$ID_FIELD")
  override val primaryKey = PrimaryKey(userId, expertMarkId, name = "UserExpertMarkPKConstraintName")
}

object ExpertRoisVos : Table(EXPERT_INSTANCES_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val researchId: Column<Int> = integer(name = RESEARCH_ID_FIELD)
  val acquisitionNumber: Column<String> = varchar("acquisition_number", 100)
  val dcmFilename: Column<String> = varchar("dcm_filename", 200)
  val instanceNumber: Column<Int> = integer("instance_number")
  val seriesNumber: Column<Int> = integer("series_number")
  val sopInstanceUid: Column<String> = varchar("sop_instance_uid", 200)
  val anatomicalLocation: Column<String> = varchar("anatomical_location", 200)
  val confidence: Column<Double> = double("confidence")
  val roiFilename: Column<String> = varchar("roi_filename", 200)
  val roiShape: Column<String> = varchar("roi_shape", 100)
  val roiType: Column<String> = varchar("roi_type", 100)
  val roiTypeIndex: Column<Int> = integer("roi_type_index")
  val taggerId: Column<String> = varchar("tagger_id", 100)
  val xCenter: Column<Double> = double("x_center")
  val xSize: Column<Double> = double("x_size")
  val yCenter: Column<Double> = double("y_center")
  val ySize: Column<Double> = double("y_size")
  val text: Column<String> = varchar("text", 200)

  override val primaryKey = PrimaryKey(id, name = "ExpertRoisPKConstraintName")
}


