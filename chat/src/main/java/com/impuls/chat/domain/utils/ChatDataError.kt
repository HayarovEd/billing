package com.impuls.chat.domain.utils

sealed interface ChatDataError : RootError {

    class CustomErrorChat(val message: String): ChatDataError

    enum class Network: ChatDataError {
        NO_INTERNET,
        UNAUTHORIZED,
        SERVER_ERROR,
        BAD_REQUEST,
        NOT_FOUND,
        REQUEST_TIMEOUT,
        DOWNLOAD_FILE_ERROR,
        UNKNOWN
    }

    enum class WebSocketErrorChat : ChatDataError {
        NOT_CONNECT,
    }

    enum class FileErrorChat : ChatDataError {
        WRITE_ERROR,
        READ_ERROR,
    }

    /*enum class CridentialError: DataError {
        EMAIL_BLANK,
        EMAIL_NOT_VALID,
        PASSWORD_BLANK,
        NAME_BLANK,
    }*/
}