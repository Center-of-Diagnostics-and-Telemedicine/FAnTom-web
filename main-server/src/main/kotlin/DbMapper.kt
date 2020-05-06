import model.*
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser(): UserModel = UserModel(
  id = this[UserVos.id],
  name = this[UserVos.name],
  password = this[UserVos.password],
  role = this[UserVos.role]
)

fun ResultRow.toResearch(): ResearchModel = ResearchModel(
  id = this[ResearchVos.id],
  accessionNumber = this[ResearchVos.accessionNumber],
  studyInstanceUID = this[ResearchVos.studyInstanceUID],
  studyID = this[ResearchVos.studyID],
  protocol = this[ResearchVos.protocol],
  accessionNumberBase = this[ResearchVos.accessionNumber_base],
  accessionNumberLastDigit = this[ResearchVos.accessionNumber_lastDigit],
  jsonName = this[ResearchVos.json_name],
  doctor1 = this[ResearchVos.doctor1],
  doctor2 = this[ResearchVos.doctor2],
  posInBlock = this[ResearchVos.pos_in_block]
)

fun ResultRow.toUserResearch(): UserResearchModel = UserResearchModel(
  userId = this[UserResearchVos.userId],
  researchId = this[UserResearchVos.researchId],
  seen = this[UserResearchVos.seen] == 1,
  done = this[UserResearchVos.done] == 1
)

fun ResultRow.toMark(): MarkModel = MarkModel(
  userId = this[MarkVos.userId],
  researchId = this[MarkVos.researchId],
  ctType = this[MarkVos.ctType],
  leftPercent = this[MarkVos.leftPercent],
  rightPercent = this[MarkVos.rightPercent]
)