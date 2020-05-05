package client.domain.repository

import model.*

interface ResearchRepository : Repository {

  suspend fun getResearches(): List<Research>
  suspend fun initResearch(researchId: Int): ResearchSlicesSizesData
  suspend fun getSlice(
    researchId: Int,
    black: Double,
    white: Double,
    gamma: Double,
    type: Int,
    mipMethod: Int,
    slyceNumber: Int,
    aproxSize: Int
  ): String

  suspend fun createMark(researchId: Int, selectedArea: AreaToSave): SelectedArea
  suspend fun deleteMark(areaId: Int)
  suspend fun getMarks(researchId: Int): List<SelectedArea>
  suspend fun updateMark(selectedArea: SelectedArea): SelectedArea
  suspend fun getHounsfieldData(axialCoord: Int, frontalCoord: Int, sagittalCoord: Int): Double
  suspend fun closeResearch(
    ctType: CTType,
    leftPercent: Int,
    rightPercent: Int,
    researchId: Int
  )
  suspend fun closeSession()
}