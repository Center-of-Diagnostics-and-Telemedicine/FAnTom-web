package model

sealed class Presets(val name: String, val valueName: String) {

  object SoftTissue : Presets(name = "Мягкие ткани", valueName = SOFT_TISSUE)
  object Vessels : Presets(name = "Сосуды", valueName = VESSELS)
  object Bones : Presets(name = "Кости", valueName = BONES)
  object Brain : Presets(name = "Мозг", valueName = BRAIN)
  object Lungs : Presets(name = "Легкие", valueName = LUNGS)

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
