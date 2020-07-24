package model

sealed class Filter(
  val name: String,
  val icon: String
) {
  object All : Filter(
    name = "Все исследования",
    icon = "all_inbox"
  )

  object NotSeen : Filter(
    name = "Непросмотренные",
    icon = "visibility_off"
  )

  object Seen : Filter(
    name = "Просмотренные",
    icon = "visibility"
  )

  object Done : Filter(
    name = "Оконченные",
    icon = "done"
  )
}
