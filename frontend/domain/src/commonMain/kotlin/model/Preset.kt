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

  object DX1 : Presets(
    name = "[D]",
    valueName = DX_1,
    black = 0,
    white = 256
  )

  object DX2 : Presets(
    name = "[2]",
    valueName = DX_2,
    black = 0,
    white = 40
  )

  object DX3 : Presets(
    name = "[3]",
    valueName = DX_3,
    black = 0,
    white = 80
  )

  object DX4 : Presets(
    name = "[4]",
    valueName = DX_4,
    black = 0,
    white = 160
  )

  object DX5 : Presets(
    name = "[5]",
    valueName = DX_5,
    black = 0,
    white = 320
  )

  object DX6 : Presets(
    name = "[6]",
    valueName = DX_6,
    black = 0,
    white = 640
  )

  object DX7 : Presets(
    name = "[7]",
    valueName = DX_7,
    black = 0,
    white = 1280
  )

  object DX8 : Presets(
    name = "[8]",
    valueName = DX_8,
    black = 0,
    white = 2560
  )

  object DX9 : Presets(
    name = "[9]",
    valueName = DX_9,
    black = 0,
    white = 5120
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
        DX_1 -> DX1
        DX_2 -> DX2
        DX_3 -> DX3
        DX_4 -> DX4
        DX_5 -> DX5
        DX_6 -> DX6
        DX_7 -> DX7
        DX_8 -> DX8
        DX_9 -> DX9
        else -> throw NoSuchElementException("cant build Preset with valueName $valueName")
      }
    }
  }
}

fun initialPresets(researchType: ResearchType): List<Presets> =
  when (researchType) {
    ResearchType.CT -> ctPresets()
    ResearchType.MG -> mgPresets()
    ResearchType.DX -> dxPresets()
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

fun dxPresets(): List<Presets> = listOf(
  Presets.DX1,
  Presets.DX2,
  Presets.DX3,
  Presets.DX4,
  Presets.DX5,
  Presets.DX6,
  Presets.DX7,
  Presets.DX8,
  Presets.DX9,
)
