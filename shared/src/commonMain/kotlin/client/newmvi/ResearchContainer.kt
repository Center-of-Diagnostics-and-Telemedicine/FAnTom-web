package client.newmvi

import com.badoo.reaktive.observable.*
import com.badoo.reaktive.scheduler.computationScheduler
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.publish.PublishSubject
import model.*
import client.*
import kotlin.math.*

object ResearchContainer {

  //sliceSizesData listeners
  val sliceSizesDataObservable = BehaviorSubject(initialResearchSlicesSizesData())
  val axialSliceNumberObservable = BehaviorSubject(1)
  val frontalSliceNumberObservable = BehaviorSubject(1)
  val sagittalSliceNumberObservable = BehaviorSubject(1)

  //sliceSizedDataContainers
  val axialSlicesSizesDataObservable = BehaviorSubject(initialSlicesSizeData())
  val frontalSlicesSizesDataObservable = BehaviorSubject(initialSlicesSizeData())
  val sagittalSlicesSizesDataObservable = BehaviorSubject(initialSlicesSizeData())

  val researchIdObservable: BehaviorSubject<Int> = BehaviorSubject(-1)

  //menu
  val blackValueObservable = BehaviorSubject(INITIAL_BLACK)
  val whiteValueObservable = BehaviorSubject(INITIAL_WHITE)
  val blackValueListener = PublishSubject<Double>()
  val whiteValueListener = PublishSubject<Double>()

  val axialBlackAndWhiteObservable = BehaviorSubject(INITIAL_BLACK to INITIAL_WHITE)
  val frontalBlackAndWhiteObservable = BehaviorSubject(INITIAL_BLACK to INITIAL_WHITE)
  val sagittalBlackAndWhiteObservable = BehaviorSubject(INITIAL_BLACK to INITIAL_WHITE)

  val axialContrastBrightnessListener = PublishSubject<MouseMoveData>()
  val frontalContrastBrightnessListener = PublishSubject<MouseMoveData>()
  val sagittalContrastBrightnessListener = PublishSubject<MouseMoveData>()

  val axialContrastBrightnessEndListener = PublishSubject<MouseData>()
  val frontalContrastBrightnessEndListener = PublishSubject<MouseData>()
  val sagittalContrastBrightnessEndListener = PublishSubject<MouseData>()

  val axialContentRatioObservable = BehaviorSubject(1.0)
  val frontalContentRatioObservable = BehaviorSubject(1.0)
  val sagittalContentRatioObservable = BehaviorSubject(1.0)

  val gammaValueObservable = BehaviorSubject(INITIAL_GAMMA)
  val mipMethodObservable = BehaviorSubject(MIP_METHOD_TYPE_NO_MIP)
  val mipValueObservable = BehaviorSubject(INITIAL_MIP_VALUE)

  val showMipValueObservable = PublishSubject<Boolean>()

  val presetObservable: BehaviorSubject<Preset> = BehaviorSubject(Preset.PRESET_LUNGS)

  val selectedAreaObservable = BehaviorSubject<Int>(-1)
  //listeners for new painted Area
  val axialNewCircleObservable: PublishSubject<Circle> = PublishSubject()
  val frontalNewCircleObservable: PublishSubject<Circle> = PublishSubject()
  val sagittalNewCircleObservable: PublishSubject<Circle> = PublishSubject()
  //newAreaToSaveNotifier
  val newAreaToSaveObservable: PublishSubject<AreaToSave> = PublishSubject()
  val areaToUpdateObservable = PublishSubject<SelectedArea>()

  //listener for new SeletedArea
  val newAreaObservable: PublishSubject<SelectedArea> = PublishSubject()
  //selectedAreasContainer
  val areasObservable = BehaviorSubject<List<SelectedArea>>(listOf())

  val selectAreaObservable = PublishSubject<Int>()
  val deleteAreaObservable = PublishSubject<Int>()
  val areaDeletedObservable = PublishSubject<Int>()
  val updateMarkObservable = PublishSubject<UpdateMarkModel>()

  val axialCirclesObservable = BehaviorSubject<List<CircleShape>>(listOf())
  val frontalCirclesObservable = BehaviorSubject<List<CircleShape>>(listOf())
  val sagittalCirclesObservable = BehaviorSubject<List<CircleShape>>(listOf())

  val axialMoveRectsObservable = BehaviorSubject<List<MoveRect>>(listOf())
  val frontalMoveRectsObservable = BehaviorSubject<List<MoveRect>>(listOf())
  val sagittalMoveRectsObservable = BehaviorSubject<List<MoveRect>>(listOf())

  val axialLinesObservable = BehaviorSubject(initialLines(SLYCE_TYPE_AXIAL))
  val frontalLinesObservable = BehaviorSubject(initialLines(SLYCE_TYPE_FRONTAL))
  val sagittalLinesObservable = BehaviorSubject(initialLines(SLYCE_TYPE_SAGITTAL))

  val axialMouseDataObservable = PublishSubject<MouseData>()
  val frontalMouseDataObservable = PublishSubject<MouseData>()
  val sagittalMouseDataObservable = PublishSubject<MouseData>()

  val axialPositionDataObservable = PublishSubject<PositionData>()
  val frontalPositionDataObservable = PublishSubject<PositionData>()
  val sagittalPositionDataObservable = PublishSubject<PositionData>()

  val axialMouseMoveObservable = PublishSubject<MouseMoveData>()
  val frontalMouseMoveObservable = PublishSubject<MouseMoveData>()
  val sagittalMouseMoveObservable = PublishSubject<MouseMoveData>()

  val axialMouseClickObservable = PublishSubject<MouseClickData>()
  val frontalMouseClickObservable = PublishSubject<MouseClickData>()
  val sagittalMouseClickObservable = PublishSubject<MouseClickData>()

  val axialMouseDownObservable = PublishSubject<MouseClickData>()
  val frontalMouseDownObservable = PublishSubject<MouseClickData>()
  val sagittalMouseDownObservable = PublishSubject<MouseClickData>()

  val deleteClickObservable = PublishSubject<Boolean>()

  val mouseUpListener = PublishSubject<MouseData>()

  val axialSliceNumberMoveObservable = PublishSubject<Int>()
  val frontalSliceNumberMoveObservable = PublishSubject<Int>()
  val sagittalSliceNumberMoveObservable = PublishSubject<Int>()

  val changeSliceNumberByDraggingMouse = BehaviorSubject(false)

  val areaIdDrag = BehaviorSubject(-1)
  val currentMoveRectInDrag = BehaviorSubject<MoveRect?>(null)

  val axialContainerSizeChangeListener = BehaviorSubject(ContainerSizeModel(0, 0))
  val frontalContainerSizeChangeListener = BehaviorSubject(ContainerSizeModel(0, 0))
  val sagittalContainerSizeChangeListener = BehaviorSubject(ContainerSizeModel(0, 0))

  val changeCutTypeListener = PublishSubject<ChangeCutTypeModel>()

  val callToCloseResearchListener = PublishSubject<Boolean>()
  val callBackToResearchListListener = PublishSubject<Boolean>()
  val closeResearchObservable = PublishSubject<CloseCommands>()

  init {

    axialContainerSizeChangeListener.subscribe(onNext = {
      val heightRatio = it.height.toDouble() / axialSlicesSizesDataObservable.value.height
      val widthRatio = it.width.toDouble() / 512
      val value = min(heightRatio, widthRatio)
      axialContentRatioObservable.onNext(value)
    })

    frontalContainerSizeChangeListener.subscribe(onNext = {
      val heightRatio = it.height.toDouble() / frontalSlicesSizesDataObservable.value.height
      val widthRatio = it.width.toDouble() / 512
      frontalContentRatioObservable.onNext(min(heightRatio, widthRatio))
    })

    sagittalContainerSizeChangeListener.subscribe(onNext = {
      val heightRatio = it.height.toDouble() / sagittalSlicesSizesDataObservable.value.height
      val widthRatio = it.width.toDouble() / 512
      val min = min(heightRatio, widthRatio)
      sagittalContentRatioObservable.onNext(min)
    })

    selectAreaObservable
      .doOnBeforeNext { id ->
        areasObservable.value
          .firstOrNull { it.id == id }
          ?.let {
            axialSliceNumberObservable.onNext(round(it.z).toInt())
            frontalSliceNumberObservable.onNext(round(it.y).toInt())
            sagittalSliceNumberObservable.onNext(round(it.x).toInt())
          }
      }
      .subscribe(onNext = selectedAreaObservable::onNext)
    mipMethodObservable
      .subscribe(onNext = {
        if (it != MIP_METHOD_TYPE_NO_MIP)
          showMipValueObservable.onNext(true)
        else
          showMipValueObservable.onNext(false)
      })

    sliceSizesDataObservable
      .subscribe(onNext = ResearchContainer::initResearch)

    newAreaObservable
      .map { newArea ->
        areasObservable.value.toList().addOrReplace(newArea) { it.id == newArea.id }
      }
      .subscribe(onNext = areasObservable::onNext)

    updateMarkObservable
      .map { newArea ->
        areasObservable.value.toList().firstOrNull { it.id == newArea.id }?.let {
          areaToUpdateObservable.onNext(
            it.copy(
              x = newArea.x ?: it.x,
              y = newArea.y ?: it.y,
              z = newArea.z ?: it.z,
              type = newArea.type ?: it.type,
              radius = newArea.radius ?: it.radius,
              size = newArea.size ?: it.size,
              comment = newArea.comment ?: it.comment
            )
          )
        }
      }
      .subscribe()

//    areasObservable
//      .subscribe(onNext = ::updateAreas)

    areaDeletedObservable
      .map { id ->
        areasObservable.value.filter { it.id != id }.toList()
      }
      .subscribe(onNext = areasObservable::onNext)

    axialSliceNumberObservable
      .subscribe(onNext = {
        //        updateAreas(areasObservable.value)
        updateLinesByAxial(it)
      })

    frontalSliceNumberObservable
      .subscribe(onNext = {
        //        updateAreas(areasObservable.value)
        updateLinesByFrontal(it)
      })

    sagittalSliceNumberObservable
      .subscribe(onNext = {
        //        updateAreas(areasObservable.value)
        updateLinesBySagittal(it)
      })

    merge(
      axialContentRatioObservable,
      frontalContentRatioObservable,
      sagittalContentRatioObservable
    )
      .subscribe(onNext = {
        updateLinesByAxial(axialSliceNumber = axialSliceNumberObservable.value)
        updateLinesByFrontal(frontalSliceNumber = frontalSliceNumberObservable.value)
        updateLinesBySagittal(sagittalSliceNumber = sagittalSliceNumberObservable.value)
//        updateAreas(areasObservable.value)
      })

    axialMouseDataObservable
      .map {
        val height = axialSlicesSizesDataObservable.value.height
        val cutHorizontalRatio = sagittalSlicesSizesDataObservable.value.maxFramesSize.toDouble() / height
        val cutVerticalRatio = frontalSlicesSizesDataObservable.value.maxFramesSize.toDouble() / height
        val cutToScreenRation = axialContentRatioObservable.value
        PositionData(
          x = (it.x * cutHorizontalRatio / cutToScreenRation).toInt(),
          y = (it.y * cutVerticalRatio / cutToScreenRation).toInt(),
          z = axialSliceNumberObservable.value
        )
      }
      .subscribe(onNext = axialPositionDataObservable::onNext)

    frontalMouseDataObservable
      .map {
        val isReversed = sliceSizesDataObservable.value.reversed
        val height = frontalSlicesSizesDataObservable.value.height
        val y = if (isReversed) height - it.y else it.y
        val cutVerticalRatio = axialSlicesSizesDataObservable.value.maxFramesSize.toDouble() / height
        val cutToScreenRatio = frontalContentRatioObservable.value
        val z = y * cutVerticalRatio / cutToScreenRatio
        PositionData(
          x = (it.x / cutToScreenRatio).toInt(),
          y = frontalSliceNumberObservable.value,
          z = z.toInt()
        )
      }
      .subscribe(onNext = frontalPositionDataObservable::onNext)

    sagittalMouseDataObservable
      .map {
        val isReversed = sliceSizesDataObservable.value.reversed
        val height = sagittalSlicesSizesDataObservable.value.height
        val y = if (isReversed) height - it.y else it.y
        val cutToScreenRatio = sagittalContentRatioObservable.value
        val cutVerticalRatio = axialSlicesSizesDataObservable.value.maxFramesSize.toDouble() / height
        PositionData(
          x = sagittalSliceNumberObservable.value,
          y = (it.x / cutToScreenRatio).toInt(),
          z = (y * cutVerticalRatio / cutToScreenRatio).toInt()
        )
      }
      .subscribe(onNext = sagittalPositionDataObservable::onNext)

    axialNewCircleObservable
      .filter { it.radius > 0.0 }
      .map {
        val cutToScreenRatio = axialContentRatioObservable.value
        it.copy(
          centerX = it.centerX / cutToScreenRatio,
          centerY = it.centerY / cutToScreenRatio,
          radius = it.radius / cutToScreenRatio
        )
      }
      .map {
        AreaToSave(
          x = it.centerX,
          y = it.centerY,
          z = axialSliceNumberObservable.value.toDouble(),
          radius = it.radius,
          size = (it.radius * 2) * axialSlicesSizesDataObservable.value.pixelLength
        )
      }
      .subscribe(onNext = newAreaToSaveObservable::onNext)

    frontalNewCircleObservable
      .filter { it.radius > 0.0 }
      .map {
        val cutToScreenRatio = frontalContentRatioObservable.value
        it.copy(
          centerX = it.centerX / cutToScreenRatio,
          centerY = it.centerY / cutToScreenRatio,
          radius = it.radius / cutToScreenRatio
        )
      }
      .map {
        val height = frontalSlicesSizesDataObservable.value.height
        val isReversed = sliceSizesDataObservable.value.reversed
        val y = if (isReversed) height - it.centerY else it.centerY
        AreaToSave(
          x = it.centerX,
          y = frontalSliceNumberObservable.value.toDouble(),
          z = y * axialSlicesSizesDataObservable.value.maxFramesSize / height,
          radius = it.radius,
          size = (it.radius * 2) * frontalSlicesSizesDataObservable.value.pixelLength
        )
      }
      .subscribe(onNext = newAreaToSaveObservable::onNext)


    sagittalNewCircleObservable
      .filter { it.radius > 0.0 }
      .map {
        val cutToScreenRatio = sagittalContentRatioObservable.value
        it.copy(
          centerX = it.centerX / cutToScreenRatio,
          centerY = it.centerY / cutToScreenRatio,
          radius = it.radius / cutToScreenRatio
        )
      }
      .map {
        val height = sagittalSlicesSizesDataObservable.value.height
        val isReversed = sliceSizesDataObservable.value.reversed
        val y = if (isReversed) height - it.centerY else it.centerY
        AreaToSave(
          x = sagittalSliceNumberObservable.value.toDouble(),
          y = it.centerX,
          z = y * axialSlicesSizesDataObservable.value.maxFramesSize / height,
          radius = it.radius,
          size = (it.radius * 2) * sagittalSlicesSizesDataObservable.value.pixelLength
        )
      }
      .subscribe(onNext = newAreaToSaveObservable::onNext)

    presetObservable
      .subscribe(
        onNext = {
          val black: Double
          val white: Double
          when (it) {
            Preset.PRESET_SOFT_TISSUE -> {
              black = -140.0
              white = 260.0
            }
            Preset.PRESET_VESSELS -> {
              black = 0.0
              white = 600.0
            }
            Preset.PRESET_BONES -> {
              black = -500.0
              white = 1000.0
            }
            Preset.PRESET_BRAIN -> {
              black = 0.0
              white = 80.0
            }
            Preset.PRESET_LUNGS -> {
              black = -1150.0
              white = 350.0
            }
          }

          val blackAndWhite = black to white
          axialBlackAndWhiteObservable.onNext(blackAndWhite)
          frontalBlackAndWhiteObservable.onNext(blackAndWhite)
          sagittalBlackAndWhiteObservable.onNext(blackAndWhite)
          blackValueObservable.onNext(black)
          whiteValueObservable.onNext(white)

        })

    blackValueListener
      .doOnBeforeNext { blackValueObservable.onNext(it) }
      .debounce(300, computationScheduler)
      .subscribe(onNext = {
        axialBlackAndWhiteObservable.onNext(axialBlackAndWhiteObservable.value.copy(first = it))
        frontalBlackAndWhiteObservable.onNext(frontalBlackAndWhiteObservable.value.copy(first = it))
        sagittalBlackAndWhiteObservable.onNext(sagittalBlackAndWhiteObservable.value.copy(first = it))
      })

    whiteValueListener
      .doOnBeforeNext { whiteValueObservable.onNext(it) }
      .debounce(300, computationScheduler)
      .subscribe(onNext = {
        axialBlackAndWhiteObservable.onNext(axialBlackAndWhiteObservable.value.copy(second = it))
        frontalBlackAndWhiteObservable.onNext(frontalBlackAndWhiteObservable.value.copy(second = it))
        sagittalBlackAndWhiteObservable.onNext(sagittalBlackAndWhiteObservable.value.copy(second = it))
      })

    axialContrastBrightnessListener
      .subscribe(onNext = {
        val old = axialBlackAndWhiteObservable.value
        val black = old.first - it.deltaY - it.deltaX
        val white = old.second - it.deltaY + it.deltaX
        axialBlackAndWhiteObservable.onNext(black to white)
        blackValueObservable.onNext(black)
        whiteValueObservable.onNext(white)
      })
    frontalContrastBrightnessListener
      .subscribe(onNext = {
        val old = frontalBlackAndWhiteObservable.value
        val black = old.first - it.deltaY - it.deltaX
        val white = old.second - it.deltaY + it.deltaX
        frontalBlackAndWhiteObservable.onNext(black to white)
        blackValueObservable.onNext(black)
        whiteValueObservable.onNext(white)
      })
    sagittalContrastBrightnessListener
      .subscribe(onNext = {
        val old = sagittalBlackAndWhiteObservable.value
        val black = old.first - it.deltaY - it.deltaX
        val white = old.second - it.deltaY + it.deltaX
        sagittalBlackAndWhiteObservable.onNext(black to white)
        blackValueObservable.onNext(black)
        whiteValueObservable.onNext(white)
      })

    axialContrastBrightnessEndListener
      .subscribe(onNext = {
        frontalBlackAndWhiteObservable.onNext(axialBlackAndWhiteObservable.value)
        sagittalBlackAndWhiteObservable.onNext(axialBlackAndWhiteObservable.value)
      })

    frontalContrastBrightnessEndListener
      .subscribe(onNext = {
        axialBlackAndWhiteObservable.onNext(frontalBlackAndWhiteObservable.value)
        sagittalBlackAndWhiteObservable.onNext(frontalBlackAndWhiteObservable.value)
      })

    sagittalContrastBrightnessEndListener
      .subscribe(onNext = {
        axialBlackAndWhiteObservable.onNext(sagittalBlackAndWhiteObservable.value)
        frontalBlackAndWhiteObservable.onNext(sagittalBlackAndWhiteObservable.value)
      })

    axialSliceNumberMoveObservable
      .map {
        axialSliceNumberObservable.value + it
      }
      .filter { it > 0 && it < axialSlicesSizesDataObservable.value.maxFramesSize }
      .subscribe(onNext = axialSliceNumberObservable::onNext)

    frontalSliceNumberMoveObservable
      .map {
        frontalSliceNumberObservable.value + it
      }
      .filter { it > 0 && it < frontalSlicesSizesDataObservable.value.maxFramesSize }
      .subscribe(onNext = frontalSliceNumberObservable::onNext)

    sagittalSliceNumberMoveObservable
      .map {
        sagittalSliceNumberObservable.value + it
      }
      .filter { it > 0 && it < sagittalSlicesSizesDataObservable.value.maxFramesSize }
      .subscribe(onNext = sagittalSliceNumberObservable::onNext)

    axialMouseClickObservable
      .doOnBeforeNext { data ->
        if (data.altKey) {
          axialCirclesObservable.value
            .firstOrNull {
              data.y < it.y + it.radius && data.y > it.y - it.radius
                && data.x < it.x + it.radius && data.x > it.x - it.radius
            }
            ?.let { circle ->
              areasObservable.value
                .firstOrNull { it.id == circle.areaId }
                ?.let {
                  axialSliceNumberObservable.onNext(round(it.z).toInt())
                  frontalSliceNumberObservable.onNext(round(it.y).toInt())
                  sagittalSliceNumberObservable.onNext(round(it.x).toInt())
                }
            }
        }
      }
      .map { data ->
        axialCirclesObservable.value.firstOrNull { area ->
          val dist = sqrt((data.x - area.x).pow(2) + (data.y - area.y).pow(2))
          dist < area.radius
        }?.areaId ?: -1
      }
      .subscribe(onNext = selectedAreaObservable::onNext)

    frontalMouseClickObservable
      .doOnBeforeNext { data ->
        if (data.altKey) {
          frontalCirclesObservable.value
            .firstOrNull {
              data.y < it.y + it.radius && data.y > it.y - it.radius
                && data.x < it.x + it.radius && data.x > it.x - it.radius
            }
            ?.let { circle ->
              areasObservable.value
                .firstOrNull { it.id == circle.areaId }
                ?.let {
                  axialSliceNumberObservable.onNext(round(it.z).toInt())
                  frontalSliceNumberObservable.onNext(round(it.y).toInt())
                  sagittalSliceNumberObservable.onNext(round(it.x).toInt())
                }
            }
        }
      }
      .map { data ->
        frontalCirclesObservable.value.firstOrNull { area ->
          val dist = sqrt((data.x - area.x).pow(2) + (data.y - area.y).pow(2))
          dist < area.radius
        }?.areaId ?: -1
      }
      .subscribe(onNext = selectedAreaObservable::onNext)

    sagittalMouseClickObservable
      .doOnBeforeNext { data ->
        if (data.altKey) {
          sagittalCirclesObservable.value
            .firstOrNull {
              data.y < it.y + it.radius && data.y > it.y - it.radius
                && data.x < it.x + it.radius && data.x > it.x - it.radius
            }
            ?.let { circle ->
              areasObservable.value
                .firstOrNull { it.id == circle.areaId }
                ?.let {
                  axialSliceNumberObservable.onNext(round(it.z).toInt())
                  frontalSliceNumberObservable.onNext(round(it.y).toInt())
                  sagittalSliceNumberObservable.onNext(round(it.x).toInt())
                }
            }
        }
      }
      .map { data ->
        sagittalCirclesObservable.value.firstOrNull { area ->
          val dist = sqrt((data.x - area.x).pow(2) + (data.y - area.y).pow(2))
          dist < area.radius
        }?.areaId ?: -1
      }
      .subscribe(onNext = selectedAreaObservable::onNext)

//    selectedAreaObservable
//      .subscribe(onNext = { updateAreas(areasObservable.value) })

    deleteClickObservable
      .subscribe(onNext = {
        if (selectedAreaObservable.value != -1) {
          deleteAreaObservable.onNext(selectedAreaObservable.value)
        }
      })

    axialMouseDownObservable
      .subscribe { data ->
        val moveRect = axialMoveRectsObservable.value.firstOrNull {
          data.y > it.top && data.y < it.top + it.sideLength
            && data.x > it.left && data.x < it.left + it.sideLength
        }
        if (moveRect != null && selectedAreaObservable.value != -1) {
          currentMoveRectInDrag.onNext(moveRect)
        } else {
          val moveCircle = axialCirclesObservable.value.firstOrNull {
            data.y < it.y + it.radius && data.y > it.y - it.radius
              && data.x < it.x + it.radius && data.x > it.x - it.radius
          }
          if (moveCircle != null && selectedAreaObservable.value != -1) {
            areaIdDrag.onNext(moveCircle.areaId)
          } else {
            changeSliceNumberByDraggingMouse.onNext(true)
          }
        }
      }

    frontalMouseDownObservable
      .subscribe { data ->
        val moveRect = frontalMoveRectsObservable.value.firstOrNull {
          data.y > it.top && data.y < it.top + it.sideLength
            && data.x > it.left && data.x < it.left + it.sideLength
        }
        if (moveRect != null && selectedAreaObservable.value != -1) {
          currentMoveRectInDrag.onNext(moveRect)
        } else {
          val moveCircle = frontalCirclesObservable.value.firstOrNull {
            data.y < it.y + it.radius && data.y > it.y - it.radius
              && data.x < it.x + it.radius && data.x > it.x - it.radius
          }
          if (moveCircle != null && selectedAreaObservable.value != -1) {
            areaIdDrag.onNext(moveCircle.areaId)
          } else {
            changeSliceNumberByDraggingMouse.onNext(true)
          }
        }
      }

    sagittalMouseDownObservable
      .subscribe { data ->
        val moveRect = sagittalMoveRectsObservable.value.firstOrNull {
          data.y > it.top && data.y < it.top + it.sideLength
            && data.x > it.left && data.x < it.left + it.sideLength
        }
        if (moveRect != null && selectedAreaObservable.value != -1) {
          currentMoveRectInDrag.onNext(moveRect)
        } else {
          val moveCircle = sagittalCirclesObservable.value.firstOrNull {
            data.y < it.y + it.radius && data.y > it.y - it.radius
              && data.x < it.x + it.radius && data.x > it.x - it.radius
          }
          if (moveCircle != null && selectedAreaObservable.value != -1) {
            areaIdDrag.onNext(moveCircle.areaId)
          } else {
            changeSliceNumberByDraggingMouse.onNext(true)
          }
        }
      }

    axialMouseMoveObservable
      .subscribe { data ->
        val moveRectInDrag = currentMoveRectInDrag.value != null && selectedAreaObservable.value != -1
        val areaInDrag = areaIdDrag.value != -1 && selectedAreaObservable.value != -1
        val changingSliceNumber = changeSliceNumberByDraggingMouse.value
        val deltaY = data.deltaY / axialContentRatioObservable.value
        val deltaX = data.deltaX / axialContentRatioObservable.value
        when {
          moveRectInDrag -> areasObservable
            .value
            .firstOrNull { it.id == currentMoveRectInDrag.value?.areaId }
            ?.let {
              when (currentMoveRectInDrag.value?.type) {
                MoveRectType.TOP -> newAreaObservable.onNext(
                  it.copy(
                    radius = it.radius - deltaY,
                    size = (it.radius - deltaY) * 2 * axialSlicesSizesDataObservable.value.pixelLength
                  )
                )
                MoveRectType.BOTTOM -> newAreaObservable.onNext(
                  it.copy(
                    radius = it.radius + deltaY,
                    size = (it.radius + deltaY) * 2 * axialSlicesSizesDataObservable.value.pixelLength
                  )
                )
                MoveRectType.RIGHT -> newAreaObservable.onNext(
                  it.copy(
                    radius = it.radius + deltaX,
                    size = (it.radius + deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
                  )
                )
                MoveRectType.LEFT -> newAreaObservable.onNext(
                  it.copy(
                    radius = it.radius - deltaX,
                    size = (it.radius - deltaX) * 2 * axialSlicesSizesDataObservable.value.pixelLength
                  )
                )
              }
            }
          areaInDrag -> areasObservable
            .value
            .firstOrNull { it.id == areaIdDrag.value }
            ?.let {
              newAreaObservable.onNext(it.copy(x = it.x + deltaX, y = it.y + deltaY))
            }
          changingSliceNumber -> {
            axialSliceNumberMoveObservable.onNext(data.deltaY.toInt())
          }
        }
      }

    frontalMouseMoveObservable
      .subscribe { data ->
        val moveRectInDrag = currentMoveRectInDrag.value != null && selectedAreaObservable.value != -1
        val areaInDrag = areaIdDrag.value != -1 && selectedAreaObservable.value != -1
        val changingSliceNumber = changeSliceNumberByDraggingMouse.value
        val isReversed = sliceSizesDataObservable.value.reversed
        val delY = data.deltaY / frontalContentRatioObservable.value
        val deltaY = if (isReversed) -delY else delY
        val deltaX = data.deltaX / frontalContentRatioObservable.value
        when {
          moveRectInDrag -> {
            areasObservable
              .value
              .firstOrNull { it.id == currentMoveRectInDrag.value?.areaId }
              ?.let {
                when (currentMoveRectInDrag.value?.type) {
                  MoveRectType.TOP -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius - delY,
                      size = (it.radius - delY) * 2 * frontalSlicesSizesDataObservable.value.pixelLength
                    )
                  )
                  MoveRectType.BOTTOM -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius + delY,
                      size = (it.radius + delY) * 2 * frontalSlicesSizesDataObservable.value.pixelLength
                    )
                  )
                  MoveRectType.RIGHT -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius + deltaX,
                      size = (it.radius + deltaX) * 2 * frontalSlicesSizesDataObservable.value.pixelLength
                    )
                  )
                  MoveRectType.LEFT -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius - deltaX,
                      size = (it.radius - deltaX) * 2 * frontalSlicesSizesDataObservable.value.pixelLength
                    )
                  )
                }
              }
          }
          areaInDrag -> {
            areasObservable
              .value
              .firstOrNull { it.id == areaIdDrag.value }
              ?.let {
                newAreaObservable.onNext(it.copy(x = it.x + deltaX, z = it.z + deltaY))
              }
          }
          changingSliceNumber -> {
            frontalSliceNumberMoveObservable.onNext(delY.toInt())
          }
        }
      }

    sagittalMouseMoveObservable
      .subscribe { data ->
        val moveRectInDrag = currentMoveRectInDrag.value != null && selectedAreaObservable.value != -1
        val areaInDrag = areaIdDrag.value != -1 && selectedAreaObservable.value != -1
        val changingSliceNumber = changeSliceNumberByDraggingMouse.value
        val isReversed = sliceSizesDataObservable.value.reversed
        val delY = data.deltaY / sagittalContentRatioObservable.value
        val deltaY = if (isReversed) -delY else delY
        val deltaX = data.deltaX / sagittalContentRatioObservable.value
        when {
          moveRectInDrag -> {
            areasObservable
              .value
              .firstOrNull { it.id == currentMoveRectInDrag.value?.areaId }
              ?.let {
                when (currentMoveRectInDrag.value?.type) {
                  MoveRectType.TOP -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius - delY,
                      size = (it.radius - delY) * 2 * sagittalSlicesSizesDataObservable.value.pixelLength
                    )
                  )
                  MoveRectType.BOTTOM -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius + delY,
                      size = (it.radius + delY) * 2 * sagittalSlicesSizesDataObservable.value.pixelLength
                    )
                  )
                  MoveRectType.RIGHT -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius + deltaX,
                      size = (it.radius + deltaX) * 2 * sagittalSlicesSizesDataObservable.value.pixelLength

                    )
                  )
                  MoveRectType.LEFT -> newAreaObservable.onNext(
                    it.copy(
                      radius = it.radius - deltaX,
                      size = (it.radius - deltaX) * 2 * sagittalSlicesSizesDataObservable.value.pixelLength

                    )
                  )
                }
              }
          }
          areaInDrag -> {
            areasObservable
              .value
              .firstOrNull { it.id == areaIdDrag.value }
              ?.let {
                newAreaObservable.onNext(it.copy(y = it.y + deltaX, z = it.z + deltaY))
              }
          }
          changingSliceNumber -> {
            sagittalSliceNumberMoveObservable.onNext(delY.toInt())
          }
        }
      }

    mouseUpListener
      .doOnBeforeNext {
        val rectInDragAreaSizeChange = currentMoveRectInDrag.value
        val areaIdInDrag = areaIdDrag.value
        when {
          rectInDragAreaSizeChange != null -> //нужно сохранить
            areasObservable.value
              .firstOrNull { it.id == rectInDragAreaSizeChange.areaId }
              ?.let {
                areaToUpdateObservable.onNext(it)
              }
          areaIdInDrag != -1 -> //нужно сохранить
            areasObservable.value
              .firstOrNull { it.id == areaIdInDrag }
              ?.let {
                areaToUpdateObservable.onNext(it)
              }
        }
      }
      .subscribe {
        areaIdDrag.onNext(-1)
        currentMoveRectInDrag.onNext(null)
        changeSliceNumberByDraggingMouse.onNext(false)
      }

    callToCloseResearchListener
      .subscribe {
        if (areasObservable.value.isNotEmpty()) {
          areasObservable.value.firstOrNull { it.type == AreaType.NO_TYPE_NODULE }.let {
            debugLog(it.toString())
            if (it != null) {
              debugLog("ReadyToClose")
              closeResearchObservable.onNext(CloseCommands.AreasNotFull)
            } else {
              debugLog("AreasNotFull")
              closeResearchObservable.onNext(CloseCommands.ReadyToClose)
            }
          }
        } else {
          closeResearchObservable.onNext(CloseCommands.ReadyToClose)
        }
      }

    callBackToResearchListListener.subscribe {
      closeResearchObservable.onNext(CloseCommands.BackToResearchList)
    }
  }

  private fun initResearch(slicesSizesData: ResearchSlicesSizesData) {

    axialSlicesSizesDataObservable.onNext(slicesSizesData.axial)
    frontalSlicesSizesDataObservable.onNext(slicesSizesData.frontal)
    sagittalSlicesSizesDataObservable.onNext(slicesSizesData.sagittal)

    axialSliceNumberObservable.onNext(slicesSizesData.axial.maxFramesSize / 2)
    frontalSliceNumberObservable.onNext(slicesSizesData.frontal.maxFramesSize / 2)
    sagittalSliceNumberObservable.onNext(slicesSizesData.sagittal.maxFramesSize / 2)

    researchIdObservable.onNext(slicesSizesData.researchId)
    blackValueObservable.onNext(INITIAL_BLACK)
    whiteValueObservable.onNext(INITIAL_WHITE)
    gammaValueObservable.onNext(INITIAL_GAMMA)
    mipMethodObservable.onNext(MIP_METHOD_TYPE_NO_MIP)
    mipValueObservable.onNext(INITIAL_MIP_VALUE)
  }

//  private fun updateAreas(list: List<SelectedArea>) {
//    val axialCirclesList = mutableListOf<CircleShape>()
//    val frontalCirclesList = mutableListOf<CircleShape>()
//    val sagittalCirclesList = mutableListOf<CircleShape>()
//
//    val axialMovableRectsList = mutableListOf<MoveRect>()
//    val frontalMovableRectsList = mutableListOf<MoveRect>()
//    val sagittalMovableRectsList = mutableListOf<MoveRect>()
//
//    val selectedArea = areasObservable.value.firstOrNull { selectedAreaObservable.value == it.id }
//
//    val frontalAxialCoefficient = frontalSlicesSizesDataObservable.value.height.toDouble() / axialSlicesSizesDataObservable.value.maxFramesSize
//    val sagittalAxialCoefficient = sagittalSlicesSizesDataObservable.value.height.toDouble() / axialSlicesSizesDataObservable.value.maxFramesSize
//
//    val axialSliceNumber = axialSliceNumberObservable.value
//    val frontalSliceNumber = frontalSliceNumberObservable.value
//    val sagittalSliceNumber = sagittalSliceNumberObservable.value
//
//    list.forEach { area ->
//      AreaToAxialCircleMapper
//        .invoke(
//          axialSliceNumber,
//          area,
//          frontalAxialCoefficient,
//          selectedAreaObservable.value,
//          axialContentRatioObservable.value
//        )?.let { circle ->
//          axialCirclesList.add(circle)
//          circle
//        }?.let { circle ->
//          if (selectedArea != null && selectedArea.id == circle.areaId) {
//            CircleToMoveRectsMapper.invoke(
//              circle = circle,
//              sideLength = 6.0,
//              color = "#fff",
//              h = abs(axialSliceNumber - round(area.z)) * frontalAxialCoefficient
//            )
//          } else {
//            null
//          }
//        }?.let { listOfMovableRects ->
//          axialMovableRectsList.addAll(listOfMovableRects)
//        }
//
//      AreaToFrontalCircleMapper
//        .invoke(
//          frontalSliceNumber,
//          area,
//          frontalAxialCoefficient,
//          selectedAreaObservable.value,
//          frontalContentRatioObservable.value,
//          height = axialSlicesSizesDataObservable.value.maxFramesSize,
//          reversed = sliceSizesDataObservable.value.reversed
//        )?.let { circle ->
//          frontalCirclesList.add(circle)
//          circle
//        }?.let { circle ->
//          if (selectedArea != null && selectedArea.id == circle.areaId) {
//            CircleToMoveRectsMapper.invoke(
//              circle = circle,
//              sideLength = 6.0,
//              color = "#fff",
//              h = abs(frontalSliceNumber - round(area.y))
//            )
//          } else {
//            null
//          }
//        }?.let { listOfMovableRects ->
//          frontalMovableRectsList.addAll(listOfMovableRects)
//        }
//
//      AreaToSagittalCircleMapper
//        .invoke(
//          sagittalSliceNumber,
//          area,
//          sagittalAxialCoefficient,
//          selectedAreaObservable.value,
//          sagittalContentRatioObservable.value,
//          height = axialSlicesSizesDataObservable.value.maxFramesSize,
//          reversed = sliceSizesDataObservable.value.reversed
//        )?.let { circle ->
//          sagittalCirclesList.add(circle)
//          circle
//        }?.let { circle ->
//          if (selectedArea != null && selectedArea.id == circle.areaId) {
//            CircleToMoveRectsMapper.invoke(
//              circle = circle,
//              sideLength = 6.0,
//              color = "#fff",
//              h = abs(sagittalSliceNumber - round(area.x))
//            )
//          } else {
//            null
//          }
//        }?.let { listOfMovableRects ->
//          sagittalMovableRectsList.addAll(listOfMovableRects)
//        }
//    }
//
//    axialCirclesObservable.onNext(axialCirclesList)
//    frontalCirclesObservable.onNext(frontalCirclesList)
//    sagittalCirclesObservable.onNext(sagittalCirclesList)
//
//    axialMoveRectsObservable.onNext(axialMovableRectsList)
//    frontalMoveRectsObservable.onNext(frontalMovableRectsList)
//    sagittalMoveRectsObservable.onNext(sagittalMovableRectsList)
//  }

  private fun updateLinesByAxial(axialSliceNumber: Int) {
    val isReversed = sliceSizesDataObservable.value.reversed
    val axialMaxFramesSize = axialSlicesSizesDataObservable.value.maxFramesSize
    val y = if (isReversed) axialMaxFramesSize - axialSliceNumber else axialSliceNumber
    val coefficient = y.toDouble() / axialMaxFramesSize
    sagittalLinesObservable.onNext(
      sagittalLinesObservable.value.copy(
        horizontal = sagittalLinesObservable.value.horizontal.copy(
          value = (sagittalSlicesSizesDataObservable.value.height * coefficient * sagittalContentRatioObservable.value).toInt()
        )
      )
    )

    frontalLinesObservable.onNext(
      frontalLinesObservable.value.copy(
        horizontal = frontalLinesObservable.value.horizontal.copy(
          value = (frontalSlicesSizesDataObservable.value.height * coefficient * frontalContentRatioObservable.value).toInt()
        )
      )
    )
  }

  private fun updateLinesByFrontal(frontalSliceNumber: Int) {
    val frontalSliceNumberDouble = frontalSliceNumber.toDouble()
    val coefficient = frontalSliceNumberDouble / frontalSlicesSizesDataObservable.value.maxFramesSize
    axialLinesObservable.onNext(
      axialLinesObservable.value.copy(
        horizontal = axialLinesObservable.value.horizontal.copy(
          value = (axialSlicesSizesDataObservable.value.height * coefficient * axialContentRatioObservable.value).toInt()
        )
      )
    )

    sagittalLinesObservable.onNext(
      sagittalLinesObservable.value.copy(
        vertical = sagittalLinesObservable.value.vertical.copy(
          value = (frontalSlicesSizesDataObservable.value.maxFramesSize * coefficient * sagittalContentRatioObservable.value).toInt()
        )
      )
    )
  }

  private fun updateLinesBySagittal(sagittalSliceNumber: Int) {
    val doubleValue = sagittalSliceNumber.toDouble()
    val coefficient = doubleValue / sagittalSlicesSizesDataObservable.value.maxFramesSize
    axialLinesObservable.onNext(
      axialLinesObservable.value.copy(
        vertical = axialLinesObservable.value.vertical.copy(
          value = (sagittalSlicesSizesDataObservable.value.maxFramesSize * coefficient * axialContentRatioObservable.value).toInt()
        )
      )
    )

    frontalLinesObservable.onNext(
      frontalLinesObservable.value.copy(
        vertical = frontalLinesObservable.value.vertical.copy(
          value = (sagittalSlicesSizesDataObservable.value.maxFramesSize * coefficient * frontalContentRatioObservable.value).toInt()
        )
      )
    )
  }
}
