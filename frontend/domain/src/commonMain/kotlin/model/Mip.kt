package model


interface HasIntValue {
  val value: Int
}

sealed class Mip(
  val name: String,
  val valueName: String,
  val intValue: Int
) {
  object No : Mip(
    name = "Без",
    valueName = NO_MIP,
    intValue = 2
  )

  data class Average(override val value: Int = 0) : Mip(
    name = "Среднее",
    valueName = AVERAGE,
    intValue = 0
  ), HasIntValue

  data class Max(override val value: Int = 0) : Mip(
    name = "Максимальное",
    valueName = MAXVALUE,
    intValue = 1
  ), HasIntValue

  companion object {

    val initial = No

    fun build(valueName: String): Mip {
      return when (valueName) {
        NO_MIP -> No
        AVERAGE -> Average()
        MAXVALUE -> Max()
        else -> throw NoSuchElementException("cant build Mip with valueName $valueName")
      }
    }
  }
}
