package controller

import io.ktor.application.ApplicationCall
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import lib.MarkTomogrammObject
import model.*
import util.debugLog
import java.util.*

interface ResearchController {
    suspend fun init(call: ApplicationCall)
    suspend fun getAccessionNames(call: ApplicationCall)
    suspend fun getSlice(call: ApplicationCall)
    suspend fun hounsfield(call: ApplicationCall)
}

class ResearchControllerImpl : ResearchController {

    override suspend fun init(call: ApplicationCall) {
        val id = call.parameters[ID]
        if (id != null && id.isNotEmpty()) {
            debugLog("id is: $id")
            try {
                MarkTomogrammObject.loadNewCtByAccessionNumber(id)
                call.respond(initSlicesData())
            } catch (e: Exception) {
                e.printStackTrace()
                respondError(call)
            }
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }

    override suspend fun getAccessionNames(call: ApplicationCall) {
        try {
            val accessionNames = MarkTomogrammObject.getAccNames().toList()
//      debugLog(accessionNames.toString())
            call.respond(AccessionNamesResponse(accessionNames))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }

    override suspend fun getSlice(call: ApplicationCall) {
        val request = call.receive<SliceRequest>()
        val slice = MarkTomogrammObject.getSlice(
            request.black,
            request.white,
            request.gamma,
            request.type,
            request.mipMethod,
            request.sliceNumber,
            request.mipValue
        )
        if (slice != null) {
            val message = Base64.getEncoder().encode(slice)
            call.respond(message)
        } else {
            respondError(call)
        }
    }

    override suspend fun hounsfield(call: ApplicationCall) {
        val params = call.request.queryParameters
        val axialCoord = params[TYPE_AXIAL]?.toInt() ?: 0
        val frontalCoord = params[TYPE_FRONTAL]?.toInt() ?: 0
        val sagittalCoord = params[TYPE_SAGITTAL]?.toInt() ?: 0

        debugLog("axial = $axialCoord, frontal = $frontalCoord, sagital = $sagittalCoord")

        val value = MarkTomogrammObject.getPointHU(
            axialCoord,
            frontalCoord,
            sagittalCoord
        )

        call.respond(HounsfieldResponse(value))
    }

    private suspend fun respondError(call: ApplicationCall) {
        call.respond(
            status = HttpStatusCode.InternalServerError,
            message = "Произошла ошибка"
        )
    }

    private fun initSlicesData(): ResearchInitResponse {
        return ResearchInitResponse(
            axialReal = MarkTomogrammObject.getRealValue(SLYCE_TYPE_AXIAL),
            axialInterpolated = MarkTomogrammObject.getInterpolatedValue(SLYCE_TYPE_AXIAL),
            frontalReal = MarkTomogrammObject.getRealValue(SLYCE_TYPE_FRONTAL),
            frontalInterpolated = MarkTomogrammObject.getInterpolatedValue(SLYCE_TYPE_FRONTAL),
            sagittalReal = MarkTomogrammObject.getRealValue(SLYCE_TYPE_SAGITTAL),
            sagittalInterpolated = MarkTomogrammObject.getInterpolatedValue(SLYCE_TYPE_SAGITTAL),
            pixelLength = MarkTomogrammObject.getPixelLengthCoef(),
            reversed = MarkTomogrammObject.getOriginalPixelCoordinate(SLYCE_TYPE_AXIAL, 0, true) > 0
        )
    }

}