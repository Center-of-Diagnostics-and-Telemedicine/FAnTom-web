package model

data class CovidMarkModel(
  val userId: Int,
  val researchId: Int,
  val rightUpperLobeValue: Int,
  val middleLobeValue: Int,
  val rightLowerLobeValue: Int,
  val leftUpperLobeValue: Int,
  val leftLowerLobeValue: Int,
)

fun CovidMarkEntity.toCovidMarkModel(userId: Int, researchId: Int): CovidMarkModel {
  return CovidMarkModel(
    userId = userId,
    researchId = researchId,
    rightUpperLobeValue = covidMarkData.rightUpperLobeValue,
    middleLobeValue = covidMarkData.middleLobeValue,
    rightLowerLobeValue = covidMarkData.rightLowerLobeValue,
    leftUpperLobeValue = covidMarkData.leftUpperLobeValue,
    leftLowerLobeValue = covidMarkData.leftLowerLobeValue,
  )
}

fun CovidMarkModel.toCovidMarkEntity(): CovidMarkEntity {
  return CovidMarkEntity(
    covidMarkData = CovidMarkData(
      rightUpperLobeValue = rightUpperLobeValue,
      middleLobeValue = middleLobeValue,
      rightLowerLobeValue = rightLowerLobeValue,
      leftUpperLobeValue = leftUpperLobeValue,
      leftLowerLobeValue = leftLowerLobeValue,
    )
  )
}