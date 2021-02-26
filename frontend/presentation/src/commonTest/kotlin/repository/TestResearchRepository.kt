package repository

import model.*
import model.ResearchApiExceptions.ResearchInitializationException
import testHounsfield
import testImage
import testResearchInitModelCT
import testResearchInitModelDX
import testResearchInitModelMG
import testResearches
import testToken

class TestResearchRepository : ResearchRepository {

  override val token: suspend () -> String = { testToken }

  override suspend fun getResearches(): List<Research> {
    return testResearches
  }

  override suspend fun getFiltered(filter: Filter, category: Category): List<Research> {
    val filteredByVisibility = when (filter) {
      Filter.All -> testResearches
      Filter.NotSeen -> testResearches.filter { it.seen.not() }
      Filter.Seen -> testResearches.filter { it.seen }
      Filter.Done -> testResearches.filter { it.done }
    }
    return when (category) {
      Category.All -> filteredByVisibility
      else -> filteredByVisibility.filter { it.category == category.name }
    }
  }

  override suspend fun initResearch(researchId: Int): ResearchSlicesSizesDataNew {
    val research = testResearches.firstOrNull { it.id == researchId }
      ?: throw ResearchInitializationException
    return when (research.modality) {
      CT_RESEARCH_MODALITY -> testResearchInitModelCT.toResearchSlicesSizesData()
      DX_RESEARCH_MODALITY -> testResearchInitModelDX.toResearchSlicesSizesData()
      MG_RESEARCH_MODALITY -> testResearchInitModelMG.toResearchSlicesSizesData()
      else -> throw NotImplementedError("your modality not implemented")
    }
  }

  override suspend fun getSlice(
    researchId: Int,
    black: Int,
    white: Int,
    gamma: Double,
    type: Int,
    mipMethod: Int,
    sliceNumber: Int,
    aproxSize: Int,
    width: Int,
    height: Int
  ): String {
    return testImage
  }

  override suspend fun getHounsfieldData(
    sliceNumber: Int,
    type: Int,
    mipMethod: Int,
    mipValue: Int,
    horizontal: Int,
    vertical: Int,
    width: Int,
    height: Int
  ): Double {
    return testHounsfield
  }


  override suspend fun confirmCtTypeForResearch(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  ) {
  }

  override suspend fun closeSession(researchId: Int) {
  }

  override suspend fun closeResearch(researchId: Int) {
  }
}
