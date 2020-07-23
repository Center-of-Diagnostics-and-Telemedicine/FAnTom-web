package model

data class CovidMark(
  val userId: Int,
  val researchId: Int,
  val ctType: Int,
  val leftPercent: Int,
  val rightPercent: Int
)

fun ConfirmCTTypeRequest.toMarkModel(userId: Int): CovidMark {
  return CovidMark(
    userId = userId,
    researchId = researchId,
    ctType = ctType,
    leftPercent = leftPercent,
    rightPercent = rightPercent
  )
}