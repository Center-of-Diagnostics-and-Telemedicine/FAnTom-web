package mapper

import controller.CutController.Input
import controller.CutController.Output
import store.cut.CutStore.*
import store.draw.DrawStore
import store.shapes.ShapesStore
import view.CutView.Model

val cutStateToCutModel: State.() -> Model? = {
  Model(
    slice = slice,
    sliceNumber = sliceNumber
  )
}

val inputToCutIntent: Input.() -> Intent? = {
  when (this) {
    is Input.BlackChanged -> Intent.HandleBlackChanged(blackValue = value)
    is Input.WhiteChanged -> Intent.HandleWhiteChanged(whiteValue = value)
    is Input.GammaChanged -> Intent.HandleGammaChanged(gammaValue = value)
    is Input.MipMethodChanged -> Intent.HandleMipChanged(mip = value)
    is Input.MipValueChanged -> Intent.HandleMipValueChanged(mipValue = value)
    is Input.PresetChanged -> Intent.HandlePresetChanged(presets = preset)
    is Input.SliceNumberChanged -> Intent.HandleSliceNumberChange(sliceNumber = sliceNumber)
    is Input.ExternalSliceNumberChanged ->
      Intent.HandleExternalSliceNumberChanged(externalCut = cut, sliceNumber = sliceNumber)
    is Input.Marks -> Intent.HandleMarks(list)
    is Input.ChangeSliceNumberByMarkCenter -> Intent.ChangeSliceNumberByMarkCenter(mark)
    Input.Idle -> null
  }
}

val cutLabelToCutOutput: Label.() -> Output? = {
  when (this) {
    is Label.SliceNumberChanged -> Output.SliceNumberChanged(sliceNumber, cut)
    is Label.CircleDrawn -> Output.CircleDrawn(circle, sliceNumber, cut)
    is Label.SelectMark -> Output.SelectMark(mark)
    is Label.CenterMark -> Output.CenterMark(mark)
    is Label.UnselectMark -> Output.UnselectMark(mark)
    is Label.ContrastBrightnessChanged -> Output.ContrastBrightnessChanged(black, white)
    is Label.MarkUpdate -> Output.UpdateMark(mark)
    is Label.UpdateMarkWithSave -> Output.UpdateMarkWithSave(mark)
    is Label.OpenFullCut -> Output.OpenFullCut(cut)
    is Label.StartClick -> null
    is Label.ExternalSliceNumberChanged -> null
    is Label.Marks -> null
    Label.StopMoving -> null
  }
}

val drawLabelToCutIntent: DrawStore.Label.() -> Intent? = {
  when (this) {
    is DrawStore.Label.Drawn -> Intent.HandleCircleDrawn(circle = circle)
    is DrawStore.Label.ChangeContrastBrightness -> Intent.ChangeContrastBrightness(deltaX, deltaY)
    DrawStore.Label.ContrastBrightnessChanged -> Intent.ContrasBrightnessChanged
    is DrawStore.Label.ChangeSlice -> Intent.ChangeSliceNumberByDraw(deltaDicomY)
    DrawStore.Label.StopMove -> Intent.HandleStopMoving
    is DrawStore.Label.StartClick -> Intent.HandleStartClick(startDicomX, startDicomY)
    is DrawStore.Label.OpenFullCut -> Intent.OpenFullCut
    is DrawStore.Label.CenterMarkClick -> null
    is DrawStore.Label.MouseMove -> null
    is DrawStore.Label.MoveInClick -> null
  }
}

val shapesLabelToCutIntent: ShapesStore.Label.() -> Intent? = {
  when (this) {
    is ShapesStore.Label.SelectMark -> Intent.HandleMarkSelected(mark)
    is ShapesStore.Label.CenterMark -> Intent.HandleMarkCenter(mark)
    is ShapesStore.Label.UnselectMark -> Intent.HandleMarkUnselect(mark)
    is ShapesStore.Label.UpdateMark -> Intent.HandleMarkUpdate(mark)
    is ShapesStore.Label.UpdateMarkWithSave -> Intent.HandleMarkUpdateWithSave(mark)
  }
}
