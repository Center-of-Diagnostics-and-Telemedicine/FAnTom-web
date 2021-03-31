package research.expert

import com.ccfraser.muirwik.components.*
import com.ccfraser.muirwik.components.button.mButton
import com.ccfraser.muirwik.components.form.MFormControlMargin
import com.ccfraser.muirwik.components.form.MFormControlVariant
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.transitions.mCollapse
import kotlinx.css.*
import kotlinx.css.properties.border
import model.*
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
  val color = Color(model.color)
  styledDiv {
    css {
      boxSizing = BoxSizing.borderBox
      border(2.px, BorderStyle.solid, color)
      backgroundColor = color.darken(30)
    }
    mListItem(
      primaryText = model.roiModel.roiType,
      secondaryText = model.roiModel.text,
      onClick = { handlePanelClick() },
      selected = expand
    ) {
      css{
        if(expand){
          backgroundColor = color.lighten(10)
        }
      }
      if (expand) mIcon("expand_less") else mIcon("expand_more")
    }

    mCollapse(show = expand && fullWidth) {
      mList(disablePadding = true) {
        model.expertQuestions.forEach { expertQuestion ->
          mListItem(selected = false) {
            mListItemText(primary = builder2.span { +"${expertQuestion.questionText}" })
          }
          styledDiv {
            css {
              paddingLeft = 4.spacingUnits
              paddingRight = 4.spacingUnits
            }
            when (expertQuestion) {
              is ExpertQuestion.MarkApprove -> buttonsAnswers(
                expertQuestion.variants as ButtonsAnswerVariant,
                expertQuestion.value
              ) { key, value -> handleAnswerChanged(ExpertQuestion.MarkApprove(key)) }

              is ExpertQuestion.DrawNewMark -> buttonsAnswers(
                expertQuestion.variants as ButtonsAnswerVariant,
                expertQuestion.value
              ) { key, value -> handleAnswerChanged(ExpertQuestion.DrawNewMark(key)) }

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

              is ExpertQuestion.MarkApprove -> checkBoxAnswers(
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
            }.let {}
            mDivider()
          }
        }
      }
    }
  }
}

fun RBuilder.buttonsAnswers(
  variants: ButtonsAnswerVariant,
  value: Int?,
  onChangeVariant: (Int, String) -> Unit
) {
  variants.variants.forEach {
    mButton(
      caption = it.value,
      onClick = { _ -> if (it.key != value) onChangeVariant(it.key, it.value) }
    )
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
