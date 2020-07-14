package dao

import PlanarMarksVos
import model.MarkData
import model.MarkDomain
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toPlanarMark

class PlanarMarksDao : MarksDaoFacade {
  override suspend fun get(id: Int): MarkDomain? {
    return transaction {
      PlanarMarksVos
        .select { PlanarMarksVos.id eq id }
        .firstOrNull()
        ?.toPlanarMark()
    }
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<MarkDomain> {
    return transaction {
      PlanarMarksVos
        .select { PlanarMarksVos.researchId.eq(researchId).and(PlanarMarksVos.userId.eq(userId)) }
        .map(ResultRow::toPlanarMark)
    }
  }

  override suspend fun save(mark: MarkData, userrId: Int, researchhId: Int): Int {
    return transaction {
      PlanarMarksVos.insert {
        it[userId] = userrId
        it[researchId] = researchhId
        it[x] = mark.x
        it[y] = mark.y
        it[radius] = mark.radius
        it[size] = mark.size
        it[cutType] = mark.cutType
      } get PlanarMarksVos.id
    }
  }

  override suspend fun update(mark: MarkDomain) {
    return transaction {
      PlanarMarksVos.update(where = { PlanarMarksVos.id eq mark.id }) {
        it[x] = mark.markData.x
        it[y] = mark.markData.y
        it[radius] = mark.markData.radius
        it[size] = mark.markData.size
        it[type] = mark.type.intValue
        it[comment] = mark.comment
        it[cutType] = mark.markData.cutType
      }
    }
  }

  override suspend fun delete(id: Int) {
    return transaction {
      PlanarMarksVos.deleteWhere { PlanarMarksVos.id.eq(id) }
    }
  }

}