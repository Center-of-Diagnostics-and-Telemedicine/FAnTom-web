package model


interface HasIntValue {
  val value: Int
}

sealed class Mip(
  val name: String,
  val valueName: String
) {
  object No : Mip(
    name = "Без",
    valueName = NO_MIP
  )

  data class Average(override val value: Int) : Mip(
    name = "Среднее",
    valueName = AVERAGE
  ), HasIntValue

  data class Max(override val value: Int) : Mip(
    name = "Максимальное",
    valueName = MAXVALUE
  ), HasIntValue

  companion object {
    fun build(valueName: String): Mip {
      return when (valueName) {
        NO_MIP -> No
        AVERAGE -> Average(0)
        MAXVALUE -> Max(0)
        else -> throw NoSuchElementException("cant build Mip with valueName $valueName")
      }
    }
  }
}
