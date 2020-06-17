package dao

import MarksVos
import model.MarkData
import model.MarkDomain
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import toMark

interface MarksDaoFacade {
  suspend fun get(id: Int): MarkDomain?
  suspend fun getAll(userId: Int, researchId: Int): List<MarkDomain>
  suspend fun save(mark: MarkData, userrId: Int, researchhId: Int): Int
  suspend fun update(mark: MarkDomain)
  suspend fun delete(id: Int)
}

class MarksDao : MarksDaoFacade {
  override suspend fun get(id: Int): MarkDomain? {
    return transaction {
      MarksVos
        .select { MarksVos.id eq id }
        .firstOrNull()
        ?.toMark()
    }
  }

  override suspend fun getAll(userId: Int, researchId: Int): List<MarkDomain> {
    return transaction {
      MarksVos
        .select { MarksVos.researchId.eq(researchId).and(MarksVos.userId.eq(userId)) }
        .map(ResultRow::toMark)
    }
  }

  override suspend fun save(mark: MarkData, userrId: Int, researchhId: Int): Int {
    return transaction {
      MarksVos.insert  {
        it[userId] = userrId
        it[researchId] = researchhId
        it[x] = mark.x
        it[y] = mark.y
        it[z] = mark.z
        it[radius] = mark.radius
        it[size] = mark.size
      } get MarksVos.id
    }
  }

  override suspend fun update(mark: MarkDomain) {
    MarksVos.update(where = { MarksVos.id eq mark.id }) {
      it[x] = mark.markData.x
      it[y] = mark.markData.y
      it[z] = mark.markData.z
      it[radius] = mark.markData.radius
      it[size] = mark.markData.size
      it[type] = mark.type.intValue
      it[comment] = mark.comment
    }
  }

  override suspend fun delete(id: Int) {
    return transaction {
      MarksVos.deleteWhere { MarksVos.id.eq(id) }
    }
  }

}
