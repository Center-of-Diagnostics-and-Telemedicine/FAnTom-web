package model

sealed class Filter(
  val name: String,
  val icon: String
) {
  object All : Filter(
    name = "Все исследования",
    icon = "view_list"
  )

  object NotSeen : Filter(
    name = "Непросмотренные",
    icon = "rate_review"
  )

  object Seen : Filter(
    name = "Просмотренные",
    icon = "search"
  )

  object Done : Filter(
    name = "Оконченные",
    icon = "done"
  )
}