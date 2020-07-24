package model

const val BASE_ERROR = "Произошла ошибка сервера. Пожалуйста, попробуйте снова"
const val INVALID_AUTH_CREDENTIALS = "Неверный логин или пароль"
const val AUTH_FAILED = "Не удалось авторизоваться"
const val INVALID_REGISTER_LOGIN = "Неверный логин (3 и более символов)"
const val INVALID_REGISTER_PASSWORD = "Неверный пароль (4 и более символов)"
const val INVALID_REGISTER_LOGIN_SIMBOLS = "Использованы запрещенные символы для логина"
const val INVALID_REGISTER_ROLE = "Роль не найдена"
const val USER_EXISTS_ERROR = "Пользователь с таким логином уже существует. Попробуйте другой логин"
const val REGISTER_FAILED = "Не удалось зарегистрировать пользователя"
const val USER_RESEARCHES_LIST_FAILED = "Не удалось получить список исследований"
const val RESEARCH_NOT_FOUND = "Не удалось найти исследование"
const val RESEARCH_INITIALIZATION_FAILED = "Не удалось инициализировать исследование"
const val RESEARCH_DATA_FETCH_FAILED = "Не удалось получить данные исследования"
const val SESSION_EXPIRED = "Сессия работы с исследованием истекла. Пожалуйста, обновите страницу"
const val INCORRECT_SLICE_NUMBER = "Некорректный номер среза"
const val INCORRECT_AXIAL_COORD = "Некорректная координата по аксиальному срезу"
const val INCORRECT_FRONTAL_COORD = "Некорректный координата по фронтальному срезу"
const val INCORRECT_SAGITTAL_COORD = "Некорректный координата по сагиттальному срезу"
const val HOUNSFIELD_FETCH_ERROR = "Не удалось получить единицы Хаунсфилда"
const val SESSION_CLOSE_FAILED = "Не удалось закончить сессию"
const val GET_SLICE_FAILED = "Не удалось получить срез"
const val CREATE_MARK_FAILED = "Не удалось создать отметку"
const val NOT_INITIALIZED_YET = "Not initialized yet"
const val MARKS_FETCH_EXCEPTION = "Не удалось получить отметки"
const val MARK_CREATE_EXCEPTION = "Не удалось создать отметку"
const val MARK_UPDATE_EXCEPTION = "Не удалось изменить отметку"
const val MARK_DELETE_EXCEPTION = "Не удалось удалить отметку"
const val RESEARCH_CLOSE_FAILED = "Не удалось закрыть исследование"
const val MARK_TYPE_NOT_SET = "У отметки отсутствует тип. Пожалуйста, выбирите тип отметки"

