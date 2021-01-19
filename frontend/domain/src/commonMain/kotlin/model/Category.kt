package model

sealed class Category(
  val name: String
) {

  object CT : Category(
    name = CT_RESEARCH_CATEGORY
  )

  object MR : Category(
    name = MR_RESEARCH_CATEGORY
  )

  object MG : Category(
    name = MG_RESEARCH_CATEGORY
  )

  object DX : Category(
    name = DX_RESEARCH_CATEGORY
  )

  object Covid : Category(
    name = COVID_RESEARCH_CATEGORY
  )

  object All : Category(
    name = "Все"
  )

}

val allCategories = listOf(
  Category.All,
  Category.CT,
  Category.MR,
  Category.MG,
  Category.DX,
  Category.Covid
)