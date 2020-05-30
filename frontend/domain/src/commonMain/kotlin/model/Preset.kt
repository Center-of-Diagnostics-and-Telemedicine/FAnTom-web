package model

sealed class Presets(val name: String, val valueName: String, val black: Int, val white: Int) {

  object SoftTissue : Presets(
    name = "Мягкие ткани",
    valueName = SOFT_TISSUE,
    black = -140,
    white = 260
  )

  object Vessels : Presets(
    name = "Сосуды",
    valueName = VESSELS,
    black = 0,
    white = 600
  )

  object Bones : Presets(
    name = "Кости",
    valueName = BONES,
    black = -500,
    white = 1000
  )

  object Brain : Presets(
    name = "Мозг",
    valueName = BRAIN,
    black = 0,
    white = 80
  )

  object Lungs : Presets(
    name = "Легкие",
    valueName = LUNGS,
    black = -1150,
    white = 350
  )

  companion object {
    fun getInitialPreset(): Presets = SoftTissue

    fun build(valueName: String): Presets {
      return when (valueName) {
        SOFT_TISSUE -> SoftTissue
        VESSELS -> Vessels
        BONES -> Bones
        BRAIN -> Brain
        LUNGS -> Lungs
        else -> throw NoSuchElementException("cant build Preset with valueName $valueName")
      }
    }
  }
}
