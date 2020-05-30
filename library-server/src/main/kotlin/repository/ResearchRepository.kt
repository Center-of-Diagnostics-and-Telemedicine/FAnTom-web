package repository

import lib.MarkTomogrammObject
import model.*
import util.LibraryState
import util.NotInitializedException
import java.util.*

interface ResearchRepository {
  fun initLib(dataStorePath: String)
  fun initResearch(accessionName: String)
  fun getInitialData(): ResearchInitModel
  fun getSlice(params: SliceRequest): String
  fun hounsfield(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Double
}

@ExperimentalStdlibApi
class ResearchRepositoryImpl(private val markTomogramm: MarkTomogrammObject) : ResearchRepository {

  override fun initLib(dataStorePath: String) {
    return markTomogramm.init(dataStorePath)
  }

  override fun initResearch(accessionName: String) {
    markTomogramm.loadNewCtByAccessionNumber(accessionName)
  }

  override fun getSlice(params: SliceRequest): String {
    return when (markTomogramm.state) {
      is LibraryState.ResearchInitialized -> {
        val slice = markTomogramm.getSlice(
          black = params.black.toDouble(),
          white = params.white.toDouble(),
          gamma = params.gamma,
          sliceType = params.sliceType,
          mipMethod = params.mipMethod,
          sliceNumber = params.sliceNumber,
          aproxSize = params.mipValue
        )
        Base64.getEncoder().encode(slice).decodeToString()
      }
      else -> throw NotInitializedException()
    }
  }

  override fun hounsfield(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Double {
    return when (markTomogramm.state) {
      is LibraryState.ResearchInitialized -> {
        markTomogramm.getPointHU(
          axialCoord = axialCoord,
          frontalCoord = frontalCoord,
          sagittalCoord = sagittalCoord
        )
      }
      else -> throw NotInitializedException()
    }
  }

  override fun getInitialData(): ResearchInitModel {
    return ResearchInitModel(
      axialReal = markTomogramm.getRealValue(SLYCE_TYPE_AXIAL),
      axialInterpolated = markTomogramm.getInterpolatedValue(SLYCE_TYPE_AXIAL),
      frontalReal = markTomogramm.getRealValue(SLYCE_TYPE_FRONTAL),
      frontalInterpolated = markTomogramm.getInterpolatedValue(SLYCE_TYPE_FRONTAL),
      sagittalReal = markTomogramm.getRealValue(SLYCE_TYPE_SAGITTAL),
      sagittalInterpolated = markTomogramm.getInterpolatedValue(SLYCE_TYPE_SAGITTAL),
      pixelLength = markTomogramm.getPixelLengthCoef(),
      reversed =
      markTomogramm.getOriginalPixelCoordinate(SLYCE_TYPE_AXIAL, 0, true) > 0
    )
  }

}
