package dao

import MultiPlanarMarksVos
import model.MarkData
import model.MarkDomain
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toMultiPlanarMark

class MultiPlanarMarksDao : MarksDaoFacade {
  override suspend fun get(id: Int): MarkDomain? {
    return transaction {
      MultiPlanarMarksVos
        .select { MultiPlanarMarksVos.id eq id }
        .firstOrNull()
        ?.toMultiPlanarMark()
    }
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<MarkDomain> {
    return transaction {
      MultiPlanarMarksVos
        .select { MultiPlanarMarksVos.researchId.eq(researchId).and(MultiPlanarMarksVos.userId.eq(userId)) }
        .map(ResultRow::toMultiPlanarMark)
    }
  }

  override suspend fun save(mark: MarkData, userrId: Int, researchhId: Int): Int {
    return transaction {
      MultiPlanarMarksVos.insert {
        it[userId] = userrId
        it[researchId] = researchhId
        it[x] = mark.x
        it[y] = mark.y
        it[z] = mark.z
        it[radius] = mark.radiusHorizontal
        it[size] = mark.size
        it[cutType] = mark.cutType
      } get MultiPlanarMarksVos.id
    }
  }

  override suspend fun update(mark: MarkDomain) {
    return transaction {
      MultiPlanarMarksVos.update(where = { MultiPlanarMarksVos.id eq mark.id }) {
        it[x] = mark.markData.x
        it[y] = mark.markData.y
        it[z] = mark.markData.z
        it[radius] = mark.markData.radiusHorizontal
        it[size] = mark.markData.size
        it[type] = mark.type.intValue
        it[comment] = mark.comment
        it[cutType] = mark.markData.cutType
      }
    }
  }

  override suspend fun delete(id: Int) {
    return transaction {
      MultiPlanarMarksVos.deleteWhere { MultiPlanarMarksVos.id.eq(id) }
    }
  }

}