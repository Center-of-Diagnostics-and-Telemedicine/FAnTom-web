package repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lib.MarkTomogrammObject
import model.*
import util.*
import java.util.*

interface ResearchRepository {
  suspend fun initResearch(accessionName: String): ApiResponse.ResearchInitResponse
  suspend fun getSlice(params: SliceRequest): ByteArray
  suspend fun hounsfield(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Double
}

class ResearchRepositoryImpl(private val markTomogramm: MarkTomogrammObject) : ResearchRepository {

  override suspend fun initResearch(accessionName: String): ApiResponse.ResearchInitResponse {
    when (markTomogramm.state) {

      is LibraryState.ReadyToInitLib -> {
        GlobalScope.launch { markTomogramm.init(data_store_path) }
        throw NotInitializedYetException()
      }
      LibraryState.InitLibProcess -> throw NotInitializedYetException()

      LibraryState.ReadyToInitResearch -> {
        GlobalScope.launch { markTomogramm.loadNewCtByAccessionNumber(accessionName) }
        throw NotInitializedYetException()
      }
      LibraryState.InitResearchProcess -> throw NotInitializedYetException()

      LibraryState.ResearchInitialized -> {
        return ApiResponse.ResearchInitResponse(
          axialReal = markTomogramm.getRealValue(SLYCE_TYPE_AXIAL),
          axialInterpolated = markTomogramm.getInterpolatedValue(SLYCE_TYPE_AXIAL),
          frontalReal = markTomogramm.getRealValue(SLYCE_TYPE_FRONTAL),
          frontalInterpolated = markTomogramm.getInterpolatedValue(SLYCE_TYPE_FRONTAL),
          sagittalReal = markTomogramm.getRealValue(SLYCE_TYPE_SAGITTAL),
          sagittalInterpolated = markTomogramm.getInterpolatedValue(SLYCE_TYPE_SAGITTAL),
          pixelLength = markTomogramm.getPixelLengthCoef(),
          reversed = markTomogramm.getOriginalPixelCoordinate(SLYCE_TYPE_AXIAL, 0, true) > 0
        )
      }
    }
  }

  override suspend fun getSlice(params: SliceRequest): ByteArray {
    return when (markTomogramm.state) {
      is LibraryState.ResearchInitialized -> {
        val slice = markTomogramm.getSlice(
          black = params.black,
          white = params.white,
          gamma = params.gamma,
          sliceType = params.type,
          mipMethod = params.mipMethod,
          sliceNumber = params.sliceNumber,
          aproxSize = params.mipValue
        )
        Base64.getEncoder().encode(slice)
      }
      else -> throw NotInitializedException()
    }
  }

  override suspend fun hounsfield(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Double {
    return when (markTomogramm.state) {
      is LibraryState.ResearchInitialized -> {
        markTomogramm.getPointHU(
          axialCoord = axialCoord,
          frontalCoord = frontalCoord,
          sagittalCoord = sagittalCoord)
      }
      else -> throw NotInitializedException()
    }
  }

}