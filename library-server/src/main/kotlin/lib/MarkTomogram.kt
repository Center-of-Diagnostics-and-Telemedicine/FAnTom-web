package lib

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.*
import util.*
import java.util.regex.Pattern

object MarkTomogrammObject {

  var state: LibraryState = LibraryState.ReadyToInitLib

  private val instance: MarkTomogramm = Native.loadLibrary(
    "FantomLibrary",
    MarkTomogramm::class.java
  )

  fun init(dataStorePath: String) {
    when (state) {
      is LibraryState.ReadyToInitLib -> {
        state = LibraryState.InitLibProcess
        instance.InitFantom_J(dataStorePath)
        debugLog("library initialized")
        state = LibraryState.ReadyToInitResearch
      }
      else -> {
        throw NotInitializedYetException()
      }
    }
  }

  fun loadNewCtByAccessionNumber(accessionNumber: String) {
    when (state) {
      is LibraryState.ReadyToInitResearch -> {
        state = LibraryState.InitResearchProcess
        instance.LoadCTbyAccession_J(accessionNumber)
        debugLog("research initialized")
        state = LibraryState.ResearchInitialized
      }
      is LibraryState.InitResearchProcess -> {
        throw NotInitializedYetException()
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getRealValue(slyceType: Int): Int {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = IntByReference()
        instance.GetTomogramDimension_J(
          result = result,
          sliceType = slyceType
        )
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getInterpolatedValue(slyceType: Int): Int {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = IntByReference()
        instance.GetScreenDimension_J(
          result = result,
          sliceType = slyceType
        )
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getCoordinateNative(sliceType: Int, nativeSlicePosition: Int): Double {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = DoubleByReference()
        instance.GetMillimeterCoordinateFromTomogramPosition_J(
          resultCoord = result,
          sliceType = sliceType,
          nativeSlicePosition = nativeSlicePosition
        )
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getCoordinateInterpolated(
    sliceType: Int,
    rescaledSliceNo: Int
  ): Double {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = DoubleByReference()
        instance.GetDatabaseCoordinateFromScreenPosition_J(
          resultCoord = result,
          sliceType = sliceType,
          rescaledSliceNo = rescaledSliceNo
        )
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getOriginalPixelCoordinate(
    sliceType: Int,
    rescaledSliceNo: Int,
    interpolateZ: Boolean
  ): Int {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = IntByReference()
        instance.GetTomogramLocationFromScreenCoordinate_J(
          resultPixelCoord = result,
          sliceType = sliceType,
          rescaledSliceNo = rescaledSliceNo,
          interpolateZ = interpolateZ
        )
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getPointHU(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Double {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = DoubleByReference()
        instance.GetPointHU_J(
          resultValue = result,
          axialCoord = axialCoord,
          frontalCoord = frontalCoord,
          sagittalCoord = sagittalCoord
        )
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getInterpolatedPixel(
    sliceType: Int,
    originalSliceNumber: Int
  ): Int {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = IntByReference()
        instance.GetScreenCoordinateFromTomogramLocation_J(
          resultRescaledPixelCoord = result,
          sliceType = sliceType,
          originalSLiceNumber = originalSliceNumber
        )
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getPixelLengthCoef(): Double {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val result = DoubleByReference()
        instance.GetPixelLengthCoefficient_J(result)
        result.value
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getAccNames(): Array<String> {
    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val ptrRef = PointerByReference()
        val length = IntByReference()

        instance.GetStudiesIDs_J(ptrRef, length)
        val pointer: Pointer? = ptrRef.value
        val result = pointer?.getByteArray(0, length.value)
        if (result != null) {
          Pattern.compile("\t").split(String(result))
        } else {
          arrayOf()
        }
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }

  fun getSlice(
    black: Double,
    white: Double,
    gamma: Double,
    sliceType: Int,
    mipMethod: Int,
    sliceNumber: Int,
    aproxSize: Int
  ): ByteArray? {

    return when (state) {
      is LibraryState.ResearchInitialized -> {
        val length = IntByReference()
        val ptrRef = PointerByReference()

        instance.GetSlice_J(
          result = ptrRef,
          length = length,
          sliceType = sliceType,
          rescaledSliceNo = sliceNumber,
          black = black,
          white = white,
          gamma = gamma,
          aproxSize = aproxSize,
          mipMethod = mipMethod
        )
        val pointer: Pointer? = ptrRef.value
        pointer?.getByteArray(0, length.value)
      }
      else -> {
        throw NotInitializedException()
      }
    }
  }
}