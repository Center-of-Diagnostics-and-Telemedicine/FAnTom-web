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

  object MG1 : Presets(
    name = "[F]",
    valueName = MG_1,
    black = 0,
    white = 4096
  )

  object MG2 : Presets(
    name = "[2]",
    valueName = MG_2,
    black = 0,
    white = 60
  )

  object MG3 : Presets(
    name = "[3]",
    valueName = MG_3,
    black = 0,
    white = 250
  )

  object MG4 : Presets(
    name = "[4]",
    valueName = MG_4,
    black = 0,
    white = 1000
  )

  object MG5 : Presets(
    name = "[5]",
    valueName = MG_5,
    black = 0,
    white = 3750
  )

  object MG6 : Presets(
    name = "[6]",
    valueName = MG_6,
    black = 0,
    white = 7500
  )

  object MG7 : Presets(
    name = "[7]",
    valueName = MG_7,
    black = 0,
    white = 15000
  )

  object MG8 : Presets(
    name = "[8]",
    valueName = MG_8,
    black = 0,
    white = 30000
  )

  object MG9 : Presets(
    name = "[9]",
    valueName = MG_9,
    black = 0,
    white = 60000
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
        MG_1 -> MG1
        MG_2 -> MG2
        MG_3 -> MG3
        MG_4 -> MG4
        MG_5 -> MG5
        MG_6 -> MG6
        MG_7 -> MG7
        MG_8 -> MG8
        MG_9 -> MG9
        else -> throw NoSuchElementException("cant build Preset with valueName $valueName")
      }
    }
  }
}

fun initialPresets(researchType: ResearchType): List<Presets> =
  when (researchType) {
    ResearchType.CT -> ctPresets()
    ResearchType.MG -> mgPresets()
    ResearchType.DX -> listOf()
  }

fun initialPreset(researchType: ResearchType): Presets =
  when (researchType) {
    ResearchType.CT -> Presets.SoftTissue
    ResearchType.MG -> Presets.MG1
    ResearchType.DX -> Presets.SoftTissue
  }

fun ctPresets(): List<Presets> = listOf(
  Presets.SoftTissue,
  Presets.Vessels,
  Presets.Bones,
  Presets.Brain,
  Presets.Lungs,
)

fun mgPresets(): List<Presets> = listOf(
  Presets.MG1,
  Presets.MG2,
  Presets.MG3,
  Presets.MG4,
  Presets.MG5,
  Presets.MG6,
  Presets.MG7,
  Presets.MG8,
  Presets.MG9,
)
