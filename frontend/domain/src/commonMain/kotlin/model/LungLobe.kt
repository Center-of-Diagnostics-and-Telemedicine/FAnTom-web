package model

data class LungLobeModel(
  val fullName: String,
  val shortName: String,
  val value: LungLobeValue?,
  val availableValues: List<LungLobeValue>
)

data class LungLobeValue(
  val name: String,
  val value: Int,
  val description: String
)

private val zeroValue = LungLobeValue("0", 0, "отсутствует")
private val firstValue = LungLobeValue("1", 1, " < 5% ")
private val secondValue = LungLobeValue("2", 2, " 5-25% ")
private val thirdValue = LungLobeValue("3", 3, " 26-50% ")
private val fourthValue = LungLobeValue("4", 4, " 51-75% ")
private val fifthValue = LungLobeValue("5", 5, " > 75% ")

private val allValues = listOf(
  zeroValue,
  firstValue,
  secondValue,
  thirdValue,
  fourthValue,
  fifthValue
)

private val rightUpperLobe = LungLobeModel(
  fullName = "Правая верхняя доля",
  shortName = "ПВД",
  value = null,
  availableValues = allValues
)

private val middleLobe = LungLobeModel(
  fullName = "Средняя доля",
  shortName = "СД",
  value = null,
  availableValues = allValues
)

private val rightLowerLobe = LungLobeModel(
  fullName = "Правая нижняя доля",
  shortName = "ПНД",
  value = null,
  availableValues = allValues
)

private val leftLowerLobe = LungLobeModel(
  fullName = "Левая нижняя доля",
  shortName = "ЛНД",
  value = null,
  availableValues = allValues
)

private val leftUpperLobe = LungLobeModel(
  fullName = "Левая Верхняя доля",
  shortName = "ЛВД",
  value = null,
  availableValues = allValues
)


val initialLungLobeModels = listOf(
  rightUpperLobe,
  middleLobe,
  rightLowerLobe,
  leftUpperLobe,
  leftLowerLobe
)

fun LungLobeModel.changeValue(newValue: Int): LungLobeModel {
  val newValueModel = allValues.firstOrNull { it.value == newValue }
  return this.copy(value = newValueModel)
}