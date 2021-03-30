package research.expert

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.transitions.mCollapse
import kotlinx.css.paddingLeft
import kotlinx.css.paddingRight
import model.CheckboxAnswerVariant
import model.ExpertQuestion
import model.RoiExpertQuestionsModel
import model.TextAnswerVariant
import org.w3c.dom.HTMLInputElement
import react.RBuilder
import react.dom.span
import styled.css
import styled.styledDiv

private val builder2 = RBuilder()

fun RBuilder.expertMarkItem(
  model: RoiExpertQuestionsModel,
  handlePanelClick: () -> Unit,
  handleAnswerChanged: (ExpertQuestion<*>) -> Unit,
  expand: Boolean,
  fullWidth: Boolean
) {
  mListItem(
    primaryText = model.roiModel.roiType,
    secondaryText = model.roiModel.text,
    onClick = { handlePanelClick() },
    selected = expand
  ) {
    if (expand) mIcon("expand_less") else mIcon("expand_more")
  }

  mCollapse(show = expand && fullWidth) {
    mList(disablePadding = true) {
      model.expertQuestions.forEach { expertQuestion ->
        mListItem(
          onClick = { /*handleVariantClick(expertQuestion.value)*/ },
          selected = false/*model.value == expertQuestion*/
        ) {
          mListItemText(primary = builder2.span { +"${expertQuestion.questionText}" })
        }
        styledDiv {
          css {
            paddingLeft = 4.spacingUnits
            paddingRight = 4.spacingUnits
          }
          when (expertQuestion) {
            is ExpertQuestion.NoduleExistence -> checkBoxAnswers(
              expertQuestion.variants as CheckboxAnswerVariant,
              expertQuestion.value
            ) { key, value -> handleAnswerChanged(ExpertQuestion.NoduleExistence(key)) }

            is ExpertQuestion.NoduleType -> checkBoxAnswers(
              expertQuestion.variants as CheckboxAnswerVariant,
              expertQuestion.value
            ) { key, value -> handleAnswerChanged(ExpertQuestion.NoduleType(key)) }

            is ExpertQuestion.NoduleDimensions -> checkBoxAnswers(
              expertQuestion.variants as CheckboxAnswerVariant,
              expertQuestion.value
            ) { key, value -> handleAnswerChanged(ExpertQuestion.NoduleDimensions(key)) }

            is ExpertQuestion.NoduleML -> checkBoxAnswers(
              expertQuestion.variants as CheckboxAnswerVariant,
              expertQuestion.value
            ) { key, value -> handleAnswerChanged(ExpertQuestion.NoduleML(key)) }

            is ExpertQuestion.NoduleExpertComment -> {
              mTextField(
                value = "",
                variant = MFormControlVariant.outlined,
                fullWidth = true,
                label = (expertQuestion.variants as TextAnswerVariant).label,
                onChange = {
                  val target = it.target as HTMLInputElement
                  handleAnswerChanged(ExpertQuestion.NoduleExpertComment(target.value))
                },
                margin = MFormControlMargin.normal
              )
            }
          }
          mDivider()
        }
      }
    }
  }
}

fun RBuilder.checkBoxAnswers(
  variants: CheckboxAnswerVariant,
  value: Int?,
  onChangeVariant: (Int, String) -> Unit
) {
  variants.variants.forEach {
    mCheckboxWithLabel(
      label = it.value,
      checked = it.key == value,
      onChange = { e, b ->
        if (it.key != value) onChangeVariant(it.key, it.value)
      }
    )
  }
}
