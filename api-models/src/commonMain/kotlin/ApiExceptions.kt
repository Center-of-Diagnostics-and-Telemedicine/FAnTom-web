package model

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