import model.*

const val testLogin = "login"
const val invalidLogin = "loign"
const val testPassword = "password"
const val invalidPassword = "passwodr"

const val testToken = "token"
const val invalidToken = "invalid_token"

val testResearches = listOf(
  Research(1, "test1", seen = false, done = false, marked = false, modality = "CT"),
  Research(2, "test2", seen = true, done = true, marked = true, modality = "CT"),
  Research(3, "test3", seen = false, done = true, marked = false, modality = "CT"),
  Research(4, "test4", seen = true, done = false, marked = false, modality = "CT"),
)

val testFilters = listOf(Filter.All, Filter.NotSeen, Filter.Seen, Filter.Done)

val testTools = listOf(Tool.MIP, Tool.Brightness, Tool.Preset)
val testGrids = listOf(
  Grid.Single(CutType.Frontal),
  Grid.TwoVertical(CutType.Frontal, CutType.Axial),
  Grid.TwoHorizontal(CutType.Frontal, CutType.Axial),
  Grid.Four(CutType.Frontal, CutType.Empty, CutType.Axial, CutType.Sagittal)
)
val testMips = listOf(Mip.No, Mip.Average(), Mip.Max())
val testPresets = listOf(
  Presets.SoftTissue,
  Presets.Vessels,
  Presets.Bones,
  Presets.Brain,
  Presets.Lungs,
)

