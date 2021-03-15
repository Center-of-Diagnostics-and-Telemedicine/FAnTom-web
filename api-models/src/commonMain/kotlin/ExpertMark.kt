package model

import kotlinx.serialization.Serializable

@Serializable
data class ExpertMarkEntity(
  val markEntity: MarkEntity,
  val expertMarkData: ExpertMarkData
)

@Serializable
data class ExpertMarkData(
  val questionsAnswers: List<ExpertQuestionAnswer>
)

@Serializable
data class ExpertQuestionAnswer(
  val questions: String,
  val answer: String
)