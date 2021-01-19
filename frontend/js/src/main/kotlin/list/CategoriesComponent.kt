package list

import com.ccfraser.muirwik.components.MChipColor
import com.ccfraser.muirwik.components.mChip
import com.ccfraser.muirwik.components.themeContext
import list.ListScreen.ListStyles.categoriesContainerCss
import list.ListScreen.ListStyles.chipMarginCss
import model.Category
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.categories(
  categories: List<Category>,
  onClick: (Category) -> Unit,
  currentCategory: Category
) {
  themeContext.Consumer { theme ->
    styledDiv {
      css(categoriesContainerCss)
      categories.forEach { category ->
        mChip(
          label = category.name,
          onClick = { onClick(category) },
          color = if (currentCategory == category) MChipColor.primary else MChipColor.default
        ) {
          css(chipMarginCss)
        }
      }
    }
  }
}