package lib

import util.debugLog
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.ptr.*
import model.data_store_path
import java.util.regex.Pattern

object MarkTomogrammObject {

  private var currentAccessionName: String = ""
  private var initialized = false

  private val instance: MarkTomogramm = Native.loadLibrary(
    "FantomLibrary",
    MarkTomogramm::class.java
  )

  private fun getInstance(): MarkTomogramm {
    if (!initialized) {
      debugLog("going to init")
      init()
    }
    return instance
  }

  private fun init() {
    val a: Int? = instance.InitFantom_J(data_store_path)
    if(a != null){
      debugLog("library initialized")
    }
    initialized = true
  }

  fun getAccNames(): Array<String> {
    val a: Array<String> = arrayOf()
    debugLog("getAccNames")
    try {
      val ptrRef = PointerByReference()
      val length = IntByReference()

      getInstance().GetStudiesIDs_J(ptrRef, length).apply {
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
      getInstance().LoadCTbyAccession_J(accessionNumber)
      debugLog("LoadNewCTbyAccession_J load success")
    }
  }

  fun getRealValue(slyceType: Int): Int {
    return try {
      val result = IntByReference()
      getInstance().GetTomogramDimension_J(result, slyceType)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  fun getInterpolatedValue(slyceType: Int): Int {
    return try {
      val result = IntByReference()
      getInstance().GetScreenDimension_J(result, slyceType)
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  fun getCoordinateNative(sliceType: Int, nativeSlicePosition: Int): Double {
    return try {
      val result = DoubleByReference()
      getInstance().GetMillimeterCoordinateFromTomogramPosition_J(
        result,
        sliceType,
        nativeSlicePosition
      )
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
      getInstance().GetDatabaseCoordinateFromScreenPosition_J(result, sliceType, rescaledSliceNo)
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
      getInstance().GetTomogramLocationFromScreenCoordinate_J(
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
      getInstance().GetPointHU_J(result, axialCoord, frontalCoord, sagittalCoord)
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
      getInstance().GetScreenCoordinateFromTomogramLocation_J(
        result,
        sliceType,
        originalSliceNumber
      )
      result.value
    } catch (e: Exception) {
      e.printStackTrace()
      -1
    }
  }

  fun getPixelLengthCoef(): Double {
    return try {
      val result = DoubleByReference()
      getInstance().GetPixelLengthCoefficient_J(result)
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

      getInstance().GetSlice_J(
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