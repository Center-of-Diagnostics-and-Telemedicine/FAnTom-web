package model

data class MarkModel(
  val userId: Int,
  val researchId: Int,
  val ctType: Int,
  val leftPercent: Int,
  val rightPercent: Int
)

fun ConfirmCTTypeRequest.toMarkModel(userId: Int): MarkModel {
  return MarkModel(
    userId = userId,
    researchId = researchId,
    ctType = ctType,
    leftPercent = leftPercent,
    rightPercent = rightPercent
  )
}