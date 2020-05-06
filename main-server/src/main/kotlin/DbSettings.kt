import model.ID_FIELD
import model.UserRole
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import util.*

object UserVos : Table(name = USER_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val name: Column<String> = varchar(name = NAME_FIELD, length = 100).uniqueIndex()
  val password: Column<String> = varchar(name = PASSWORD_FIELD, length = 200)
  val role: Column<Int> = integer(name = ROLE_FIELD).default(UserRole.DOCTOR.value)
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
  override val primaryKey = PrimaryKey(id, name = "ResearchPKConstraintName")
}

object UserResearchVos : Table("user_research") {
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer("research_$ID_FIELD")
  val seen: Column<Int> = integer(name = SEEN_FIELD).default(0)
  val done: Column<Int> = integer(name = DONE_FIELD).default(0)
  override val primaryKey = PrimaryKey(userId, researchId, name = "UserResearchPKConstraintName")
}

object MarkVos : Table(MARKS_TABLE) {
  val userId: Column<Int> = integer("user_$ID_FIELD")
  val researchId: Column<Int> = integer("research_$ID_FIELD")
  val ctType: Column<Int> = integer(CT_TYPE_FIELD)
  val leftPercent: Column<Int> = integer(LEFT_PERCENT_FIELD)
  val rightPercent: Column<Int> = integer(RIGHT_PERCENT_FIELD)
  override val primaryKey = PrimaryKey(userId, researchId, name = "UserMarkPKConstraintName")
}