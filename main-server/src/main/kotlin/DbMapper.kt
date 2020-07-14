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
  posInBlock = this[ResearchVos.pos_in_block],
  modality = this[ResearchVos.modality]
)

fun ResultRow.toUserResearch(): UserResearchModel = UserResearchModel(
  userId = this[UserResearchVos.userId],
  researchId = this[UserResearchVos.researchId],
  seen = this[UserResearchVos.seen] == 1,
  done = this[UserResearchVos.done] == 1
)

fun ResultRow.toCovidMark(): MarkModel = MarkModel(
  userId = this[CovidMarksVos.userId],
  researchId = this[CovidMarksVos.researchId],
  ctType = this[CovidMarksVos.ctType],
  leftPercent = this[CovidMarksVos.leftPercent],
  rightPercent = this[CovidMarksVos.rightPercent]
)

fun ResultRow.toMultiPlanarMark(): MarkDomain = MarkDomain(
  id = this[MultiPlanarMarksVos.id],
  markData = MarkData(
    x = this[MultiPlanarMarksVos.x],
    y = this[MultiPlanarMarksVos.y],
    z = this[MultiPlanarMarksVos.z],
    radius = this[MultiPlanarMarksVos.radius],
    size = this[MultiPlanarMarksVos.size],
    cutType = this[MultiPlanarMarksVos.cutType]
  ),
  type = MarkType
    .values()
    .firstOrNull { it.intValue == this[MultiPlanarMarksVos.type] }
    ?: MarkType.NO_TYPE_NODULE,
  comment = this[MultiPlanarMarksVos.comment],
)

fun ResultRow.toPlanarMark(): MarkDomain = MarkDomain(
  id = this[PlanarMarksVos.id],
  markData = MarkData(
    x = this[PlanarMarksVos.x],
    y = this[PlanarMarksVos.y],
    z = -1.0,
    radius = this[PlanarMarksVos.radius],
    size = this[PlanarMarksVos.size],
    cutType = this[PlanarMarksVos.cutType]
  ),
  type = MarkType
    .values()
    .firstOrNull { it.intValue == this[PlanarMarksVos.type] }
    ?: MarkType.NO_TYPE_NODULE,
  comment = this[PlanarMarksVos.comment],
)