package repository

import com.badoo.reaktive.observable.Observable
import model.*

interface MyResearchRepository {

  val token: suspend () -> String

  val research: Observable<Research?>
  val researches: Observable<List<Research>?>
  val researchData: Observable<ResearchDataModel?>

  suspend fun loadResearches()
  suspend fun applyFilter(filter: Filter, category: Category)
  suspend fun initResearch(researchId: Int)
  suspend fun closeSession(researchId: Int)
  suspend fun closeResearch(researchId: Int)

  suspend fun getSlice(model: GetSliceModel): String

}

fun GetSliceModel.toSliceRequest(): SliceRequestNew =
  SliceRequestNew(
    image = ImageModel(
      modality = modality,
      type = type,
      number = sliceNumber,
      mip = MipModel(
        mip_method = getMipMethodStringType(mipMethod),
        mip_value = aproxSize
      ),
      width = width,
      height = height,
      sop_instance_uid = sopInstanceUid
    ),
    brightness = BrightnessModel(
      black = black,
      white = white,
      gamma = gamma
    )
  )