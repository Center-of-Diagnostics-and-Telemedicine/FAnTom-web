package model

const val TOKEN = "api_token"
const val END_POINT: String = "https://fantom.npcmr.ru"
const val LOCALHOST = "http://127.0.0.1"

//const val END_POINT: String = "http://10.2.20.44:8080"
const val LOGIN_ROUTE: String = "login"
const val RESEARCH_ROUTE: String = "research"
const val MARK_ROUTE: String = "mark"
const val INIT_ROUTE: String = "init"
const val LIST_ROUTE: String = "list"
const val HOUNSFIELD_ROUTE: String = "hounsfield"
const val AUTH_CHECK_ROUTE: String = "check"
const val CLOSE_ROUTE: String = "close"
const val SESSION_ROUTE: String = "session"

const val TYPE_AXIAL = "AXIAL"
const val TYPE_FRONTAL = "FRONTAL"
const val TYPE_SAGITTAL = "SAGITTAL"

const val data_store_path = "/app/dicom"
const val ID_FIELD = "id"

const val tenMinutes: Long = 600000

/**
Типы срезов
 */
const val SLYCE_TYPE_AXIAL: Int = 0
const val SLYCE_TYPE_FRONTAL: Int = 1
const val SLYCE_TYPE_SAGITTAL: Int = 2

/**
MIP методы
 */
const val MIP_METHOD_TYPE_AVERAGE: Int = 0
const val MIP_METHOD_TYPE_MAXVALUE: Int = 1
const val MIP_METHOD_TYPE_NO_MIP: Int = 2

const val AVERAGE = "average"
const val MAXVALUE = "maxvalue"
const val NO_MIP = "no_mip"

const val INITIAL_BLACK = -1150.0
const val INITIAL_WHITE = 350.0
const val INITIAL_GAMMA = 1.0
const val INITIAL_MIP_VALUE = 0


enum class Preset(val value: Int) {
    PRESET_SOFT_TISSUE(0),
    PRESET_VESSELS(1),
    PRESET_BONES(2),
    PRESET_BRAIN(3),
    PRESET_LUNGS(4),
}

const val SOFT_TISSUE = "soft_tisue"
const val VESSELS = "vessels"
const val BONES = "bones"
const val BRAIN = "brain"
const val LUNGS = "lungs"

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
    BOTTOM
}

const val yellow = "#ffff00"
const val pink = "#ff00ff"
const val blue = "#00ffff"

const val LEFT_MOUSE_BUTTON: Short = 0
const val MIDDLE_MOUSE_BUTTON: Short = 1
const val RIGHT_MOUSE_BUTTON: Short = 2

enum class Environment {
    DEBUG,
    PRODUCTION
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
    AUTH_FAILED(23)
}