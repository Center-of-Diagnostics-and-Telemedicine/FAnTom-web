package mapper

import controller.CutController.Input
import controller.CutController.Output
import store.cut.CutStore.*
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
  }
}

val cutLabelToCutOutput: Label.() -> Output = {
  when (this) {
    is Label.SliceNumberChanged -> Output.SliceNumberChanged(sliceNumber, cut)
  }
}
