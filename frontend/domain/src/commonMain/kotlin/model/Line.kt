package model

sealed class Line(
  val lineColor: String,
  val lineValue: Int
) {
  data class HorizontalLine(
    val color: String,
    val value: Int
  ) : Line(color, value)

  data class VerticalLine(
    val color: String,
    val value: Int
  ) : Line(color, value)
}
