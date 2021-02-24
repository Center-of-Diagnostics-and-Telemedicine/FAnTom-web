package model

data class UserResearchModel(
  val userId: Int,
  val researchId: Int,
  val seen: Boolean,
  val done: Boolean
)