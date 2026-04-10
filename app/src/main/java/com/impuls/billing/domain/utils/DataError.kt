package com.impuls.billing.domain.utils

sealed interface DataError : RootError {

    class CustomError(val message: String): DataError

    enum class Network: DataError {
        UNAUTHORIZED,
        SERVER_ERROR,
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNKNOWN,
        NO_INTERNET
    }

    enum class SerializationError: DataError {
       FORMAT_ERROR,
    }

    enum class ValidationError: DataError {
        EMAIL_BLANK,
        EMAIL_NOT_VALID,
        PASSWORD_BLANK,
        LOGIN_PASSWORD_INCORRECT
    }

    enum class  DataStore: DataError {
        ERROR_READ_DATA,
        ERROR_WRITE_DATA
    }

    enum class LocalDateBase : DataError {
        ERROR_READ_DATA,
        ERROR_WRITE_DATA
    }

    enum class SuccessWork: DataError {
        SUCCESS_UPDATE_PASSWORD
    }
}