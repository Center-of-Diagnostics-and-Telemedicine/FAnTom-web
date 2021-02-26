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


}
