package dao

import ResearchVos
import model.ResearchModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import repository.dao.ResearchDaoFacade
import toResearch

class ResearchDao() : ResearchDaoFacade {

  override suspend fun getAll(): List<ResearchModel> {
    return transaction {
      ResearchVos
        .selectAll()
        .map(ResultRow::toResearch)
    }
  }

  override suspend fun getResearchById(researchId: Int): ResearchModel? {
    return transaction {
      ResearchVos
        .select { ResearchVos.id eq researchId }
        .firstOrNull()
        ?.toResearch()
    }
  }

  override suspend fun getResearchByAccessionNumber(accessionNumber: String): ResearchModel? {
    return transaction {
      ResearchVos
        .select { ResearchVos.accessionNumber eq accessionNumber }
        .firstOrNull()
        ?.toResearch()
    }

  }

  override suspend fun createResearch(researchModel: ResearchModel) {
    return transaction {
      ResearchVos.insert {
        it[accessionNumber] = researchModel.accessionNumber
        it[studyInstanceUID] = researchModel.studyInstanceUID
        it[studyID] = researchModel.studyID
        it[protocol] = researchModel.protocol
        it[accessionNumber_base] = researchModel.accessionNumberBase
        it[accessionNumber_lastDigit] = researchModel.accessionNumberLastDigit
        it[json_name] = researchModel.jsonName
        it[doctor1] = researchModel.doctor1
        it[doctor2] = researchModel.doctor2
        it[pos_in_block] = researchModel.posInBlock
        it[modality] = researchModel.modality
        it[category] = researchModel.category
      }
    }
  }

  override suspend fun deleteResearch(researchId: Int) {
    transaction {
      ResearchVos.deleteWhere { ResearchVos.id eq researchId }
    }
  }

  override suspend fun updateResearch(researchModel: ResearchModel) {
    return transaction {
      ResearchVos.update(where = { ResearchVos.id eq researchModel.id }) {
        it[accessionNumber] = researchModel.accessionNumber
        it[studyInstanceUID] = researchModel.studyInstanceUID
        it[studyID] = researchModel.studyID
        it[protocol] = researchModel.protocol
        it[accessionNumber_base] = researchModel.accessionNumberBase
        it[accessionNumber_lastDigit] = researchModel.accessionNumberLastDigit
        it[json_name] = researchModel.jsonName
        it[doctor1] = researchModel.doctor1
        it[doctor2] = researchModel.doctor2
        it[pos_in_block] = researchModel.posInBlock
        it[modality] = researchModel.modality
        it[category] = researchModel.category
      }
    }
  }


}