package model

data class LungLobeModel(
  val id: Int,
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
  id = rightUpperLobeId,
  fullName = "Правая верхняя доля",
  shortName = "ПВД",
  value = null,
  availableValues = allValues
)

private val middleLobe = LungLobeModel(
  id = middleLobeId,
  fullName = "Средняя доля",
  shortName = "СД",
  value = null,
  availableValues = allValues
)

private val rightLowerLobe = LungLobeModel(
  id = rightLowerLobeId,
  fullName = "Правая нижняя доля",
  shortName = "ПНД",
  value = null,
  availableValues = allValues
)

private val leftLowerLobe = LungLobeModel(
  id = leftLowerLobeId,
  fullName = "Левая нижняя доля",
  shortName = "ЛНД",
  value = null,
  availableValues = allValues
)

private val leftUpperLobe = LungLobeModel(
  id = leftUpperLobeId,
  fullName = "Левая Верхняя доля",
  shortName = "ЛВД",
  value = null,
  availableValues = allValues
)


val initialLungLobeModels = mapOf(
  rightUpperLobeId to rightUpperLobe,
  middleLobeId to middleLobe,
  rightLowerLobeId to rightLowerLobe,
  leftUpperLobeId to leftUpperLobe,
  leftLowerLobeId to leftLowerLobe
)

fun LungLobeModel.changeValue(newValue: Int): LungLobeModel {
  val newValueModel = allValues.firstOrNull { it.value == newValue }
  return this.copy(value = newValueModel)
}

fun CovidMarkEntity.toLungLobeModelMap(): Map<Int, LungLobeModel> {
  val rightUpperLobeModel = rightUpperLobe.changeValue(covidMarkData.rightUpperLobeValue)
  val rightLowerLobeModel = rightLowerLobe.changeValue(covidMarkData.rightLowerLobeValue)
  val middleLobeModel = middleLobe.changeValue(covidMarkData.middleLobeValue)
  val leftUpperLobeModel = leftUpperLobe.changeValue(covidMarkData.leftUpperLobeValue)
  val leftLowerLobeModel = leftLowerLobe.changeValue(covidMarkData.leftLowerLobeValue)

  return mapOf(
    rightUpperLobeId to rightUpperLobeModel,
    rightLowerLobeId to rightLowerLobeModel,
    middleLobeId to middleLobeModel,
    leftUpperLobeId to leftUpperLobeModel,
    leftLowerLobeId to leftLowerLobeModel
  )
}

fun Map<Int, LungLobeModel>.toCovidMarkEntity(): CovidMarkEntity {
  val rightUpperLobeValue = this[rightUpperLobeId]
  val rightLowerLobeValue = this[rightLowerLobeId]
  val middleLobeValue = this[middleLobeId]
  val leftUpperLobeValue = this[leftUpperLobeId]
  val leftLowerLobeValue = this[leftLowerLobeId]

  return CovidMarkEntity(
    covidMarkData = CovidMarkData(
      rightUpperLobeValue = rightUpperLobeValue?.value?.value ?: noValue,
      rightLowerLobeValue = rightLowerLobeValue?.value?.value ?: noValue,
      middleLobeValue = middleLobeValue?.value?.value ?: noValue,
      leftUpperLobeValue = leftUpperLobeValue?.value?.value ?: noValue,
      leftLowerLobeValue = leftLowerLobeValue?.value?.value ?: noValue,
    )
  )
}