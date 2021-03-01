package model

const val TOKEN = "api_token"
const val END_POINT: String = "https://fantom.npcmr.ru"
const val LOCALHOST = "http://localhost"
const val MAIN_SERVER_PORT = 8081
const val MAIN_SERVER_URL = "$LOCALHOST:$MAIN_SERVER_PORT"

const val DATABASE_NAME = "fantom_mg"
const val DATABASE_HOST = "localhost"
const val DATABASE_PORT = 3306
const val DATABASE_URL =
  "jdbc:mysql://$DATABASE_HOST:$DATABASE_PORT/$DATABASE_NAME?characterEncoding=utf8&useUnicode=true&useSSL=false"
const val DATABASE_DRIVER = "com.mysql.jdbc.Driver"
const val DATABASE_USER = "root"
const val DATABASE_PASSWORD = ""

const val LOGIN_ROUTE: String = "login"
const val RESEARCH_ROUTE: String = "research"
const val MARK_ROUTE: String = "mark"
const val COVID_MARK_ROUTE: String = "covid_mark"
const val INIT_ROUTE: String = "init"
const val SLICE_ROUTE: String = "slice"
const val LIST_ROUTE: String = "list"
const val HOUNSFIELD_ROUTE: String = "hounsfield"
const val BRIGHTNESS_ROUTE: String = "brightness"
const val AUTH_CHECK_ROUTE: String = "check"
const val CLOSE_ROUTE: String = "close"
const val SESSION_ROUTE: String = "session"
const val JSON_TYPE = "json"

const val TYPE_AXIAL = "AXIAL"
const val TYPE_FRONTAL = "FRONTAL"
const val TYPE_SAGITTAL = "SAGITTAL"

const val DEFAULT_USER_NAME = "doctor"

//mac
const val localDataStorePath = "/Users/max/Documents/test"
//win
//const val localDataStorePath = "C:\\dicom\\test"

//deploy
//const val localDataStorePath = "/data/fantom/dx2"
const val dockerDataStorePath = "/app/dicom"

const val ID_FIELD = "id"

const val noValue = -1

const val tenMinutes: Long = 600000

const val libraryServerPort: Int = 5555
const val libraryServerSchema: String = "http://"
const val libraryServerDomain: String = "localhost"

const val protocolsPath = "C:\\Users\\max\\Downloads\\protocol"

const val defaultUserPassword = "password"

/**
Типы срезов
 */
const val SLICE_TYPE_CT_AXIAL: Int = 0
const val SLICE_TYPE_CT_FRONTAL: Int = 1
const val SLICE_TYPE_CT_SAGITTAL: Int = 2
const val SLICE_TYPE_MG_RCC: Int = 3
const val SLICE_TYPE_MG_LCC: Int = 4
const val SLICE_TYPE_MG_RMLO: Int = 5
const val SLICE_TYPE_MG_LMLO: Int = 6
const val SLICE_TYPE_DX_GENERIC: Int = 7
const val SLICE_TYPE_DX_POSTERO_ANTERIOR: Int = 8
const val SLICE_TYPE_DX_LEFT_LATERAL: Int = 9
const val SLICE_TYPE_DX_RIGHT_LATERAL: Int = 10

const val SHAPE_TYPE_CIRCLE = 0
const val SHAPE_TYPE_RECTANGLE = 1

/**
MIP методы
 */
const val MIP_METHOD_TYPE_AVERAGE: Int = 0
const val MIP_METHOD_TYPE_MAXVALUE: Int = 1
const val MIP_METHOD_TYPE_NO_MIP: Int = 2
const val MIP_METHOD_TYPE_MINVALUE: Int = 3

const val AVERAGE = "average"
const val MAXVALUE = "maxvalue"
const val NO_MIP = "no_mip"

const val INITIAL_BLACK = -1150.0
const val INITIAL_WHITE = 350.0
const val INITIAL_GAMMA = 1.0
const val INITIAL_MIP_VALUE = 0

const val CT_RESEARCH_MODALITY = "CT"
const val MG_RESEARCH_MODALITY = "MG"
const val DX_RESEARCH_MODALITY = "DX"

const val CT_RESEARCH_CATEGORY = "CT"
const val MR_RESEARCH_CATEGORY = "MR"
const val MG_RESEARCH_CATEGORY = "MG"
const val DX_RESEARCH_CATEGORY = "DX"
const val COVID_RESEARCH_CATEGORY = "Covid"
const val ALL_RESEARCH_CATEGORY = "Все"


enum class Preset(val value: Int) {
  PRESET_SOFT_TISSUE(0),
  PRESET_VESSELS(1),
  PRESET_BONES(2),
  PRESET_BRAIN(3),
  PRESET_LUNGS(4),
}

const val SOFT_TISSUE = "soft_tissue"
const val VESSELS = "vessels"
const val BONES = "bones"
const val BRAIN = "brain"
const val LUNGS = "lungs"
const val MG_1 = "MG_F"
const val MG_2 = "MG_2"
const val MG_3 = "MG_3"
const val MG_4 = "MG_4"
const val MG_5 = "MG_5"
const val MG_6 = "MG_6"
const val MG_7 = "MG_7"
const val MG_8 = "MG_8"
const val MG_9 = "MG_9"
const val DX_1 = "DX_D"
const val DX_2 = "DX_2"
const val DX_3 = "DX_3"
const val DX_4 = "DX_4"
const val DX_5 = "DX_5"
const val DX_6 = "DX_6"
const val DX_7 = "DX_7"
const val DX_8 = "DX_8"
const val DX_9 = "DX_9"

enum class LineType {
  HORIZONTAL,
  VERTICAL
}

/**
Типы очагов
 */
enum class AreaType(val value: Int) {
  //нет типа
  NO_TYPE_NODULE(-1),

  //солидный
  SOLID_NODULE(0),

  //полусолидный
  PART_SOLID_NODULE(1),

  //матовое стекло
  PURE_SUBSOLID_NODULE(2),

  //не онкология
  NOT_ONKO(3),
}

enum class MoveRectType {
  TOP,
  LEFT,
  RIGHT,
  BOTTOM,
  LEFT_TOP,
  RIGHT_TOP,
  LEFT_BOTTOM,
  RIGHT_BOTTOM
}

const val yellow = "#ffff00"
const val pink = "#ff00ff"
const val blue = "#00ffff"
const val green = "#00ff00"

const val LEFT_MOUSE_BUTTON: Short = 0
const val MIDDLE_MOUSE_BUTTON: Short = 1
const val RIGHT_MOUSE_BUTTON: Short = 2

enum class Environment {
  DEBUG,
  PRODUCTION
}

enum class ResearchType {
  CT,
  MG,
  DX
}

fun ResearchType.isPlanar(): Boolean {
  return when (this) {
    ResearchType.CT -> false
    ResearchType.MG -> true
    ResearchType.DX -> true
  }
}

enum class CutsGridType {
  SINGLE,
  TWO_VERTICAL,
  TWO_HORIZONTAL,
  THREE,
  FOUR
}

enum class Position {
  LEFT_TOP,
  RIGHT_TOP,
  LEFT_BOTTOM,
  RIGHT_BOTTOM
}

enum class UserRole(val value: Int) {
  ADMIN(0),
  DOCTOR(1),
  EXPERT(2)
}

enum class CTType {
  ZERO,
  LIGHT,
  MIDDLE,
  HIGH,
  CRITICAL,
  NON_RELEVANT
}

enum class ErrorStringCode(val value: Int) {
  BASE_ERROR(1),
  USER_EXIST_ERROR(2),
  INVALID_AUTH_CREDENTIALS(3),
  INVALID_REGISTER_LOGIN(4),
  INVALID_REGISTER_ROLE(5),
  INVALID_REGISTER_PASSWORD(6),
  INVALID_REGISTER_LOGIN_SIMBOLS(7),
  REGISTER_FAILED(8),
  USER_RESEARCHES_LIST_FAILED(9),
  RESEARCH_NOT_FOUND(10),
  RESEARCH_INITIALIZATION_FAILED(11),
  RESEARCH_DATA_FETCH_FAILED(12),
  SESSION_EXPIRED(13),
  INCORRECT_SLICE_NUMBER(14),
  INCORRECT_AXIAL_COORD(15),
  INCORRECT_FRONTAL_COORD(16),
  INCORRECT_SAGITTAL_COORD(17),
  HOUNSFIELD_ERROR(18),
  SESSION_CLOSE_FAILED(19),
  GET_SLICE_FAILED(20),
  CREATE_MARK_FAILED(21),
  NOT_INITIALIZED_YET(22),
  AUTH_FAILED(23),
  UPDATE_MARK_FAILED(24),
  GET_MARKS_FAILED(25),
  DELETE_MARK_FAILED(26),
  RESEARCH_CLOSE_FAILED(27)
}

const val rightUpperLobeId: Int = 1
const val middleLobeId: Int = 2
const val rightLowerLobeId: Int = 3
const val leftUpperLobeId: Int = 4
const val leftLowerLobeId: Int = 5


