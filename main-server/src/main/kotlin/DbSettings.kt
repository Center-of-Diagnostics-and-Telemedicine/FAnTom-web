import model.UserRole
import util.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object UserVos : Table(name = USER_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val name: Column<String> = varchar(name = NAME_FIELD, length = 100).uniqueIndex()
  val password: Column<String> = varchar(name = PASSWORD_FIELD, length = 200)
  val role: Column<Int> = integer(name = ROLE_FIELD).default(UserRole.DOCTOR.value)
  override val primaryKey = PrimaryKey(id, name = "UserPKConstraintName")
}

object ResearchVos : Table(name = RESEARCH_TABLE) {
  val id: Column<Int> = integer(name = ID_FIELD).autoIncrement()
  val name: Column<String> = varchar(name = NAME_FIELD, length = 200).uniqueIndex()
  override val primaryKey = PrimaryKey(id, name = "UserPKConstraintName")
}

object UserResearchVos : Table("user_research") {
  val userId: Column<Int> = integer("user_$ID_FIELD").primaryKey()
  val researchId: Column<Int> = integer("research_$ID_FIELD").primaryKey()
  val seen: Column<Int> = integer(name = SEEN_FIELD).default(0)
  val done: Column<Int> = integer(name = DONE_FIELD).default(0)
  override val primaryKey = PrimaryKey(userId, researchId, name = "UserResearchPKConstraintName")
}

//меняются ли файлы индексации
