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

  object Expert : Category(
    name = EXPERT_RESEARCH_CATEGORY
  )

  object DoseReport: Category(
    name = DOSE_REPORT_RESEARCH_CATEGORY
  )

  object All : Category(
    name = ALL_RESEARCH_CATEGORY
  )
}

val allCategories = listOf(
  Category.All,
  Category.CT,
  Category.MR,
  Category.MG,
  Category.DX,
  Category.Covid,
  Category.Expert,
  Category.DoseReport
)

fun Research.getCategoryByString(): Category {
  return when (category) {
    CT_RESEARCH_CATEGORY -> Category.CT
    MR_RESEARCH_CATEGORY -> Category.MR
    MG_RESEARCH_CATEGORY -> Category.MG
    DX_RESEARCH_CATEGORY -> Category.DX
    COVID_RESEARCH_CATEGORY -> Category.Covid
    EXPERT_RESEARCH_CATEGORY -> Category.Expert
    DOSE_REPORT_RESEARCH_CATEGORY -> Category.DoseReport
    else -> Category.All
  }
}