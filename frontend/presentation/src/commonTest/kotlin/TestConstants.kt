import model.*

const val testLogin = "login"
const val invalidLogin = "loign"
const val testPassword = "password"
const val invalidPassword = "passwodr"

const val testToken = "token"
const val invalidToken = "invalid_token"

val testResearches = listOf(
  Research(
    1,
    "test1",
    seen = false,
    done = false,
    marked = false,
    modality = CT_RESEARCH_MODALITY,
    category = CT_RESEARCH_CATEGORY
  ),
  Research(
    2,
    "test2",
    seen = true,
    done = true,
    marked = true,
    modality = CT_RESEARCH_MODALITY,
    category = COVID_RESEARCH_CATEGORY
  ),
  Research(
    3,
    "test3",
    seen = false,
    done = true,
    marked = false,
    modality = MG_RESEARCH_MODALITY,
    category = MG_RESEARCH_CATEGORY
  ),
  Research(
    4,
    "test4",
    seen = true,
    done = false,
    marked = false,
    modality = DX_RESEARCH_MODALITY,
    category = DX_RESEARCH_CATEGORY
  ),
)

val testResearch = testResearches.first()

val testFilters = listOf(Filter.All, Filter.NotSeen, Filter.Seen, Filter.Done)

val testTools = listOf(Tool.MIP, Tool.Brightness, Tool.Preset)

val testCategory = Category.CT
//val testGrids = listOf(
//  Grid.Single(CutType.CT_AXIAL),
//  Grid.TwoVertical(CutType.CT_FRONTAL, CutType.CT_AXIAL),
//  Grid.TwoHorizontal(CutType.CT_FRONTAL, CutType.CT_AXIAL),
//  Grid.Four(CutType.CT_FRONTAL, CutType.EMPTY, CutType.CT_AXIAL, CutType.CT_SAGITTAL)
//)
val testMips = listOf(Mip.No, Mip.Average(), Mip.Max())
val testPresets = listOf(
  Presets.SoftTissue,
  Presets.Vessels,
  Presets.Bones,
  Presets.Brain,
  Presets.Lungs,
)

val ctMarkTypes = mapOf(
  "#0_S" to MarkTypeEntity(
    EN = "Solid",
    RU = "Солидное",
    CLR = ""
  ),
  "#1_PS" to MarkTypeEntity(
    EN = "Part solid",
    RU = "Полусолидное",
    CLR = ""
  ),
  "#2_GG" to MarkTypeEntity(
    EN = "Ground Glass",
    RU = "Матовое стекло",
    CLR = ""
  )
)
val testCtAxialModalityModel = PlaneModel(
  dicomSizeH = 512,
  dicomSizeV = 512,
  dicomStepH = 0.698,
  dicomStepV = 0.698,
  nImages = 357,
  screenSizeH = 512,
  screenSizeV = 512
)
val testCtFrontalModalityModel = PlaneModel(
  dicomSizeH = 512,
  dicomSizeV = 357,
  dicomStepH = 0.698,
  dicomStepV = 0.7977563025210084,
  nImages = 512,
  screenSizeH = 512,
  screenSizeV = 408
)
val testCtSagittalModalityModel = PlaneModel(
  dicomSizeH = 512,
  dicomSizeV = 357,
  dicomStepH = 0.698,
  dicomStepV = 0.7977563025210084,
  nImages = 512,
  screenSizeH = 512,
  screenSizeV = 408,
)
val testResearchInitModelCT = ResearchInitModelNew(
  CT = CTInitModel(
    ct_axial = testCtAxialModalityModel,
    ct_frontal = testCtFrontalModalityModel,
    ct_sagittal = testCtSagittalModalityModel,
    reversed = false
  ),
  dictionary = ctMarkTypes
)

val mgMarkTypes = mapOf(
  "#0_M" to MarkTypeEntity(
    EN = "miscellaneous",
    RU = "Прочие находки",
    CLR = "#ffffc0"
  ),
  "#1_MG" to MarkTypeEntity(
    EN = "Malignant growth",
    RU = "ЗНО",
    CLR = "#ff0000"
  ),
  "#2_B" to MarkTypeEntity(
    EN = "Benign",
    RU = "Доброкачественное",
    CLR = "#00ff00"
  ),
  "#3_SC" to MarkTypeEntity(
    EN = "Suspicious calcification",
    RU = "Подозрительные кальцинаты",
    CLR = "#ffff00"
  ),
  "#4_PA" to MarkTypeEntity(
    EN = "Pathologically altered lymph nodes",
    RU = "Патологически измененные лимфоузлы",
    CLR = "#ff00ff"
  ),
  "#5_TS" to MarkTypeEntity(
    EN = "",
    RU = "Утолщение кожи",
    CLR = "#00ffff"
  )
)
val testResearchInitModelMG = ResearchInitModelNew(
  MG = MGInitModel(
    mg_lcc = PlaneModel(
      dicomSizeH = 1770,
      dicomSizeV = 2370,
      dicomStepH = 0.10000000149011612,
      dicomStepV = 0.10000000149011612,
      nImages = 1,
      screenSizeH = 1770,
      screenSizeV = 2370
    ),
    mg_lmlo = PlaneModel(
      dicomSizeH = 1770,
      dicomSizeV = 2370,
      dicomStepH = 0.10000000149011612,
      dicomStepV = 0.10000000149011612,
      nImages = 1,
      screenSizeH = 1770,
      screenSizeV = 2370,
    ),
    mg_rcc = PlaneModel(
      dicomSizeH = 1770,
      dicomSizeV = 2370,
      dicomStepH = 0.10000000149011612,
      dicomStepV = 0.10000000149011612,
      nImages = 1,
      screenSizeH = 1770,
      screenSizeV = 2370
    ),
    mg_rmlo = PlaneModel(
      dicomSizeH = 1770,
      dicomSizeV = 2370,
      dicomStepH = 0.10000000149011612,
      dicomStepV = 0.10000000149011612,
      nImages = 1,
      screenSizeH = 1770,
      screenSizeV = 2370
    ),
    reversed = false
  ),
  dictionary = mgMarkTypes
)

val testResearchInitModelDX = ResearchInitModelNew(
  DX = DXInitModel(
    dx0 = PlaneModel(
      dicomSizeH = 1024,
      dicomSizeV = 1024,
      dicomStepH = 0.171,
      dicomStepV = 0.171,
      nImages = 1,
      screenSizeH = 1024,
      screenSizeV = 1024
    ),
    reversed = false
  ),
  dictionary = mgMarkTypes
)

const val testImage = "aabbww"
const val testHounsfield = 1.0
const val testPosition = 10.0
const val testMouseWheelPosition = 10

val testMarks = listOf(
  MarkEntity(
    id = 1,
    markData = MarkData(
      x = 1.0,
      y = 1.0,
      z = 1.0,
      radiusHorizontal = 1.0,
      radiusVertical = 1.0,
      sizeHorizontal = 1.0,
      sizeVertical = 1.0,
      cutType = CutType.CT_AXIAL.intType,
      shapeType = SHAPE_TYPE_CIRCLE
    ),
    type = "",
    comment = ""
  ),
  MarkEntity(
    id = 2,
    markData = MarkData(
      x = 2.0,
      y = 2.0,
      z = 2.0,
      radiusHorizontal = 2.0,
      radiusVertical = 2.0,
      sizeHorizontal = 2.0,
      sizeVertical = 2.0,
      cutType = CutType.CT_FRONTAL.intType,
      shapeType = SHAPE_TYPE_CIRCLE
    ),
    type = "",
    comment = ""
  ),
  MarkEntity(
    id = 3,
    markData = MarkData(
      x = 3.0,
      y = 3.0,
      z = 3.0,
      radiusHorizontal = 3.0,
      radiusVertical = 3.0,
      sizeHorizontal = 3.0,
      sizeVertical = 3.0,
      cutType = CutType.CT_SAGITTAL.intType,
      shapeType = SHAPE_TYPE_CIRCLE
    ),
    type = "",
    comment = ""
  )
)
val testMark = testMarks.first().toMarkModel(ctMarkTypes)
const val testComment = "comment"

val testMarkType = MarkTypeModel(
  typeId = "#2_B",
  en = "Benign",
  ru = "Доброкачественное",
  color = "#00ff00"
)

const val testStringId = "testId"
const val testIntId = -1000

val testCut = Plane(
  type = CutType.CT_AXIAL,
  data = testCtAxialModalityModel,
  color = axialColor,
  verticalCutData = CutData(
    type = CutType.CT_FRONTAL,
    data = testCtFrontalModalityModel,
    color = frontalColor
  ),
  horizontalCutData = CutData(
    type = CutType.CT_SAGITTAL,
    data = testCtSagittalModalityModel,
    color = sagittalColor
  ),
  researchType = ResearchType.CT,
  availableCutsForChange = listOf()
)

val testCutType = CutType.CT_AXIAL
val testSliceNumber = testCut.data.nImages / 2
val testCircle = CircleModel(
  dicomX = 10.0,
  dicomY = 10.0,
  dicomWidth = 14.142135623730951,
  dicomHeight = 14.142135623730951,
  id = -1,
  highlight = false,
  isCenter = false,
  color = "#00ff00"
)

val testCovidMarkEntity = getEmptyCovidMarkEntity()
val testCovidMarkModel = testCovidMarkEntity.toLungLobeModelMap()
val testLungLobeModel = testCovidMarkModel.values.first()
val testLungLobeValue = testLungLobeModel.availableValues.first()





