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

val inputToCutIntent: Input.() -> Intent = {
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
    is Input.ExternalCircleUpdate -> Intent.HandleExternalCircleUpdate(circleToUpdate, cut)
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
    is Label.CircleUpdate -> Output.UpdateCircle(circleToUpdate, cut)
    is Label.ExternalSliceNumberChanged -> null
    is Label.Marks -> null
    is Label.ExternalCircleChanged -> null
  }
}

val drawLabelToCutIntent: DrawStore.Label.() -> Intent? = {
  when (this) {
    is DrawStore.Label.Drawn -> Intent.HandleCircleDrawn(circle = circle)
    is DrawStore.Label.ChangeContrastBrightness -> Intent.ChangeContrastBrightness(deltaX, deltaY)
    DrawStore.Label.ContrastBrightnessChanged -> Intent.ContrasBrightnessChanged
    is DrawStore.Label.ChangeSlice -> Intent.ChangeSliceNumberByDraw(deltaDicomY)
    is DrawStore.Label.OnClick -> null
    is DrawStore.Label.StartMove -> null
    is DrawStore.Label.MouseMove -> null
    is DrawStore.Label.MoveInClick -> null
    DrawStore.Label.StopMove -> null
  }
}

val shapesLabelToCutIntent: ShapesStore.Label.() -> Intent? = {
  when (this) {
    is ShapesStore.Label.SelectMark -> Intent.HandleMarkSelected(mark)
    is ShapesStore.Label.CenterMark -> Intent.HandleMarkCenter(mark)
    is ShapesStore.Label.UnselectMark -> Intent.HandleMarkUnselect(mark)
    is ShapesStore.Label.UpdateCircle -> Intent.HandleCircleUpdate(circleToUpdate, cut)
  }
}
