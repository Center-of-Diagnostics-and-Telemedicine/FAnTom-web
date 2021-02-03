package mapper

import controller.CutController.Input
import controller.CutController.Output
import store.cut.CutStore.*
import store.draw.DrawStore
import store.shapes.ShapesStore
import store.userinput.UserInputStore
import view.CutView.Event
import view.CutView.Model

val cutStateToCutModel: State.() -> Model? = {
  Model(
    slice = slice,
    sliceNumber = sliceNumber,
    mainLoading = mainLoading,
    secondaryLoading = secondaryLoading,
    error = error
  )
}

val cutEventToCutIntent: Event.() -> Intent? = {
  when (this) {
    Event.DismissError -> Intent.DismissErrorRequested
  }
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
    is Label.RectangleDrawn -> Output.RectangleDrawn(rectangle, sliceNumber, cut)
    is Label.SelectMark -> Output.SelectMark(mark)
    is Label.CenterMark -> Output.CenterMark(mark)
    is Label.UnselectMark -> Output.UnselectMark(mark)
    is Label.ContrastBrightnessChanged -> Output.ContrastBrightnessChanged(black, white)
    is Label.UpdateMarkWithoutSave -> Output.UpdateMarkWithoutSave(mark)
    is Label.UpdateMarkWithSave -> Output.UpdateMarkWithSave(mark)
    is Label.OpenFullCut -> Output.OpenFullCut(cut)
    is Label.ChangeCutType -> Output.ChangeCutType(cutType, cut)
    is Label.StartClick -> null
    is Label.ExternalSliceNumberChanged -> null
    is Label.Marks -> null
    Label.StopMoving -> null
  }
}

val drawLabelToCutIntent: DrawStore.Label.() -> Intent? = {
  when (this) {
    is DrawStore.Label.CircleDrawn -> Intent.HandleCircleDrawn(circle = circle)
    is DrawStore.Label.RectangleDrawn -> Intent.HandleRectangleDrawn(rectangle = rectangle)
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
    is ShapesStore.Label.UpdateMarkCoordinates -> Intent.HandleMarkUpdateWithoutSave(mark)
    is ShapesStore.Label.UpdateMarkWithSave -> Intent.HandleMarkUpdateWithSave(mark)
    is ShapesStore.Label.ChangeCutType -> Intent.HandleChangeCutType(cutType)
  }
}

val userInputToCutIntent: UserInputStore.Label.() -> Intent? = {
  when (this) {
    is UserInputStore.Label.MouseMove -> null
    UserInputStore.Label.StopMove -> Intent.HandleStopMoving
    is UserInputStore.Label.ChangeSlice -> Intent.ChangeSliceNumberByDraw(deltaDicomY)
    is UserInputStore.Label.ChangeContrastBrightness ->
      Intent.ChangeContrastBrightness(deltaX, deltaY)
    UserInputStore.Label.OpenFullCut -> Intent.OpenFullCut
    UserInputStore.Label.ContrastBrightnessChanged -> Intent.ContrasBrightnessChanged
  }
}
