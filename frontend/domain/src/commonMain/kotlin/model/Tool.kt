package model

sealed class Tool(
  val name: String,
  val icon: String
) {

  object MIP : Tool(
    name = "MIP",
    icon = "line_weight"
  )

  object Brightness : Tool(
    name = "Яркость и контраст",
    icon = "tonality"
  )

  object Preset : Tool(
    name = "Шаблоны",
    icon = "palette"
  )

  object Series : Tool(
    name = "Серии",
    icon = "image"
  )
}

sealed interface MyTool {

  val name: String
  val icon: String

  object Back : MyTool {
    override val name: String = "Все исследования"
    override val icon: String = "keyboard_backspace"
  }

  object MIP : MyTool {
    override val name: String = "MIP"
    override val icon: String = "line_weight"
  }

  object Brightness : MyTool {
    override val name: String = "Яркость и контраст"
    override val icon: String = "tonality"
  }

  object Preset : MyTool {
    override val name: String = "Шаблоны"
    override val icon: String = "palette"
  }

  object Close : MyTool {
    override val name: String = "Закончить исследование"
    override val icon: String = "done"
  }
}



