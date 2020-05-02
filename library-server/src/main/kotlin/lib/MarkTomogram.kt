package lib

import util.data_store_path
import util.debugLog
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.*
import java.util.regex.Pattern

object MarkTomogrammObject {

  private var currentAccessionName: String = ""

  private val instance: MarkTomogramm = Native.loadLibrary(
    "FantomLibrary",
    MarkTomogramm::class.java
  )

  fun init() {
    instance.InitFantom_J(data_store_path)
  }

  fun getAccNames(): Array<String> {
    val a: Array<String> = arrayOf()
    debugLog("getAccNames")
    try {
      val ptrRef = PointerByReference()
      val length = IntByReference()

      instance.GetStudiesIDs_J(ptrRef, length).apply {
        val pointer: Pointer? = ptrRef.value
        val result = pointer?.getByteArray(0, length.value)
        if (result != null) {
          return Pattern.compile("\t").split(String(result))
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return a
  }

  fun loadNewCtByAccessionNumber(accessionNumber: String) {
    if (currentAccessionName != accessionNumber) {
      currentAccessionName = accessionNumber
      debugLog("loadNewCtByAccessionNumber")
      instance.LoadCTbyAccession_J(accessionNumber)
      debugLog("LoadNewCTbyAccession_J load success")
    }
  }

  fun getRealValue(slyceType: Int): Int {
    return try {
      val result = IntByReference()
      instance.GetTomogramDimension_J(result, slyceType)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  fun getInterpolatedValue(slyceType: Int): Int {
    return try {
      val result = IntByReference()
      instance.GetScreenDimension_J(result, slyceType)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  fun getCoordinateNative(sliceType: Int, nativeSlicePosition: Int): Double {
    return try {
      val result = DoubleByReference()
      instance.GetMillimeterCoordinateFromTomogramPosition_J(result, sliceType, nativeSlicePosition)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1.0
    }
  }

  fun getCoordinateInterpolated(
    sliceType: Int,
    rescaledSliceNo: Int
  ): Double {
    return try {
      val result = DoubleByReference()
      instance.GetDatabaseCoordinateFromScreenPosition_J(result, sliceType, rescaledSliceNo)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1.0
    }
  }

  fun getOriginalPixelCoordinate(
    sliceType: Int,
    rescaledSliceNo: Int,
    interpolateZ: Boolean
  ): Int {
    return try {
      val result = IntByReference()
      instance.GetTomogramLocationFromScreenCoordinate_J(
        result,
        sliceType,
        rescaledSliceNo,
        interpolateZ
      )
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  fun getPointHU(
    axialCoord: Int,
    frontalCoord: Int,
    sagittalCoord: Int
  ): Double {
    return try {
      val result = DoubleByReference()
      instance.GetPointHU_J(result, axialCoord, frontalCoord, sagittalCoord)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1.0
    }
  }

  fun getInterpolatedPixel(
    sliceType: Int,
    originalSliceNumber: Int
  ): Int {
    return try {
      val result = IntByReference()
      instance.GetScreenCoordinateFromTomogramLocation_J(result, sliceType, originalSliceNumber)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  fun getPixelLengthCoef(): Double {
    return try {
      val result = DoubleByReference()
      instance.GetPixelLengthCoefficient_J(result)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1.0
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

    debugLog("getSlice, type = $sliceType")
    try {
      val length = IntByReference()
      val ptrRef = PointerByReference()

      instance.GetSlice_J(
        ptrRef,
        length,
        sliceType,
        sliceNumber,
        black,
        white,
        gamma,
        aproxSize,
        mipMethod
      ).apply {
        val pointer: Pointer? = ptrRef.value
        return pointer?.getByteArray(0, length.value)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return ByteArray(0)
  }
}