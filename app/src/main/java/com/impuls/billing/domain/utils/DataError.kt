package com.impuls.billing.domain.utils

sealed interface DataError : RootError {
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
    }

    enum class  DataStore: DataError {
        ERROR_READ_DATA,
        ERROR_WRITE_DATA
    }

    enum class LocalDateBase : DataError {
        ERROR_READ_DATA,
        ERROR_WRITE_DATA
    }
}