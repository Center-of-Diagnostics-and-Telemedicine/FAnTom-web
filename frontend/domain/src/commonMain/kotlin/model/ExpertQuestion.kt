package model

val expertQuestionsList = listOf<ExpertQuestion<*>>(
  ExpertQuestion.NoduleExistence(),
  ExpertQuestion.NoduleType(),
  ExpertQuestion.NoduleDimensions(),
  ExpertQuestion.NoduleML(),
  ExpertQuestion.NoduleExpertComment()
)

sealed class ExpertQuestion<ValueType>(
  val questionText: String,
  val variants: AnswerVariant,
  val value: ValueType?
) {

  class NoduleExistence(value: Int? = null) :
    ExpertQuestion<Int>(
      questionText = "Согласны ли вы с наличием очага в указанном месте?",
      variants = CheckboxAnswerVariant(variants = noduleExistenceVariants),
      value = value
    )

  class NoduleType(value: Int? = null) : ExpertQuestion<Int>(
    questionText = "Каков, по вашему мнению, тип очагового образования?",
    variants = CheckboxAnswerVariant(variants = noduleTypeVariants),
    value = value
  )

  class NoduleDimensions(value: Int? = null) : ExpertQuestion<Int>(
    questionText = "Согласны ли Вы со всеми оценками размеров очагов?",
    variants = CheckboxAnswerVariant(yesNoVariants),
    value = value
  )

  class NoduleML(value: Int? = null) : ExpertQuestion<Int>(
    questionText = "Считаете ли Вы, что этот очаг следует использовать для машинного обучения?",
    variants = CheckboxAnswerVariant(yesNoVariants),
    value = value
  )

  class NoduleExpertComment(value: String? = null) : ExpertQuestion<String>(
    questionText = "Комментарий (можно оставить поле пустым)",
    variants = TextAnswerVariant("комментарий (можно оставить пустым)"),
    value = value
  )

  private companion object {
    val yesNoVariants = mapOf(
      0 to "Да",
      1 to "Нет"
    )
    val noduleExistenceVariants = mapOf(
      0 to "Не согласен",
      1 to "Есть сомнения",
      2 to "Частично согласен",
      3 to "полностью согласен",
    )
    val noduleTypeVariants = mapOf(
      0 to "Солидный (С)",
      1 to "Полусолидный (П)",
      2 to "Матовое стекло (М)"
    )
  }
}

interface AnswerVariant

data class CheckboxAnswerVariant(
  val variants: Map<Int, String>
) : AnswerVariant

data class TextAnswerVariant(
  val label: String
) : AnswerVariant