package client

import model.*

val ctTypes = listOf(
  CTTypeModel(
    name = "КТ-0",
    color = "#78b980",
    description = "Норма и отсутствие КТ-признаков вирусной пневмонии на фоне типичной клинической картины и релевантного эпидемиологического анамнеза",
    ctType = CTType.ZERO
  ),
  CTTypeModel(
    name = "КТ-1",
    color = "#b7d389",
    description = "Зоны уплотнения по типу \"матового стекла\" \nВовлечение паренхимы легкого =< 25% \nЛибо отсутствие КТ-признаков на фоне типичной клинической картины и релевантного эпидемиологического анамнеза",
    ctType = CTType.LIGHT
  ),
  CTTypeModel(
    name = "КТ-2",
    color = "#fae991",
    description = "Зоны уплотнения по типу \"матового стекла\" \nВовлечение паренхимы легкого 25-50%",
    ctType = CTType.MIDDLE
  ),
  CTTypeModel(
    name = "КТ-3",
    color = "#eeac7d",
    description = "Зоны уплотнения по типу \"матового стекла\" \nЗоны консолидации \nВовлечение паренхимы легкого 50-75% \nУвеличение объема поражения 50% за 24-48 часов на фоне дыхательных нарушений, если исследования выполняются в динамике",
    ctType = CTType.HIGH
  ),
  CTTypeModel(
    name = "КТ-4",
    color = "#e4706d",
    description = "Диффузное уплотнение легочной ткани по типу \"матового стекла\" и консолидации в сочетании с ретикулярными изменениями \nГидроторакс (двусторонний, преобладает слева) \nВовлечение паренхимы легкого >= 75%",
    ctType = CTType.CRITICAL
  )
)

sealed class ResearchApiExceptions(val error: String) : Throwable() {

  object AuthFailedException : ResearchApiExceptions(AUTH_FAILED)
  object InvalidAuthCredentials : ResearchApiExceptions(INVALID_AUTH_CREDENTIALS)

  object ResearchListFetchException : ResearchApiExceptions(USER_RESEARCHES_LIST_FAILED)
  object ResearchInitializationException : ResearchApiExceptions(RESEARCH_INITIALIZATION_FAILED)
  object ResearchNotFoundException : ResearchApiExceptions(RESEARCH_NOT_FOUND)

  object SliceFetchException : ResearchApiExceptions(GET_SLICE_FAILED)
  object IncorrectSliceNumberException : ResearchApiExceptions(INCORRECT_SLICE_NUMBER)

  object HounsfieldFetchError : ResearchApiExceptions(HOUNSFIELD_FETCH_ERROR)
  object IncorrectAxialValueException : ResearchApiExceptions(INCORRECT_AXIAL_COORD)
  object IncorrectFrontalValueException : ResearchApiExceptions(INCORRECT_FRONTAL_COORD)
  object IncorrectSagittalValueException : ResearchApiExceptions(INCORRECT_SAGITTAL_COORD)

  object ConfirmCtTypeForResearchException : ResearchApiExceptions(CREATE_MARK_FAILED)

  object CloseSessionException : ResearchApiExceptions(SESSION_CLOSE_FAILED)
  object SessionExpiredException : ResearchApiExceptions(SESSION_EXPIRED)
}