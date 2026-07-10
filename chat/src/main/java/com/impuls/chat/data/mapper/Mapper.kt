package com.impuls.chat.data.mapper

import com.impuls.chat.data.remote.response.GroupDto
import com.impuls.chat.data.remote.response.create_chat_dto.ChatDto
import com.impuls.chat.data.remote.response.create_chat_dto.ConnectionDto
import com.impuls.chat.data.remote.response.create_chat_dto.FileDto
import com.impuls.chat.data.remote.response.create_chat_dto.MessageDto
import com.impuls.chat.data.remote.response.create_chat_dto.RoleDto
import com.impuls.chat.data.remote.response.create_chat_dto.UserDto
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Connection
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.Role
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.MimeTypes.convertToMimeType
import com.impuls.chat.domain.model.User
import com.impuls.chat.domain.utils.Utils
import java.io.File

fun GroupDto.convertToGroup(): Group {
    return Group(
        id = id,
        name = name,
        operId = operId
    )
}

fun RoleDto.convertToRole(): Role {
    return Role(
        id = this.id,
        name = this.name
    )
}

fun UserDto.convertToUser(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        isDisabled = this.isDisabled,
        role = this.role.convertToRole()
    )
}

fun MessageDto.convertToMessage(): Message {
    return Message(
        date = Utils.convertToLocalDateTime(this.date),
        id = this.id,
        isSupport = this.isSupport,
        text = this.text,
        statusMessage = if (this.isRead) StatusMessage.READED else  StatusMessage.DELIVERED,
        files = this.files.map { it.convertToFile() }
    )
}

fun ConnectionDto.convertToConnection(): Connection {
    return Connection(
        end = if (this.end != null) Utils.convertToLocalDateTime(this.end) else null,
        id = this.id,
        messages = this.messages?.map { it.convertToMessage() } ?: emptyList(),
        start = Utils.convertToLocalDateTime(
            this.start
        ),
        user = this.user?.convertToUser()
    )
}

fun ChatDto.convertToChat(): Chat {
    return Chat(
        connections = this.connections.map { it.convertToConnection() },
        createdDate = Utils.convertToLocalDateTime(this.createdDate),
        email = this.email,
        id = this.id,
        isOpen = this.isOpen,
        isWork = this.isWork,
        name = this.name,
        login = this.login,
        phone = this.phone,
        group = Group(
            id = this.group.id,
            name = this.group.name,
            operId = this.group.operId
        ),
        isActive = this.isActive,
        unreadMessagesCount = this.unreadMessagesCount
    )
}

fun FileDto.convertToFile(): MessageFile {
    return MessageFile(
        fileSize = fileSize,
        fileUrl = imageUrl,
        originalName = originalName,
        mimeType = convertToMimeType(mimeType)
    )
}

fun getMimeTypeExtended(file: File): String {
    val extension = file.extension.lowercase()
    return when (extension) {
        // Изображения
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "bmp" -> "image/bmp"
        "webp" -> "image/webp"
        "svg" -> "image/svg+xml"
        "heic" -> "image/heic"

        // Документы
        "pdf" -> "application/pdf"
        "doc" -> "application/msword"
        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "xls" -> "application/vnd.ms-excel"
        "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        "ppt" -> "application/vnd.ms-powerpoint"
        "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation"
        "odt" -> "application/vnd.oasis.opendocument.text"
        "ods" -> "application/vnd.oasis.opendocument.spreadsheet"
        "odp" -> "application/vnd.oasis.opendocument.presentation"
        "rtf" -> "application/rtf"
        "txt" -> "text/plain"
        "csv" -> "text/csv"
        "md" -> "text/markdown"

        // Видео
        "mp4" -> "video/mp4"
        "avi" -> "video/x-msvideo"
        "mov" -> "video/quicktime"
        "wmv" -> "video/x-ms-wmv"
        "flv" -> "video/x-flv"
        "mkv" -> "video/x-matroska"
        "webm" -> "video/webm"
        "3gp" -> "video/3gpp"

        // Аудио
        "mp3" -> "audio/mpeg"
        "wav" -> "audio/wav"
        "ogg" -> "audio/ogg"
        "aac" -> "audio/aac"
        "flac" -> "audio/flac"
        "m4a" -> "audio/mp4"

        // Архивы
        "zip" -> "application/zip"
        "rar" -> "application/vnd.rar"
        "7z" -> "application/x-7z-compressed"
        "tar" -> "application/x-tar"
        "gz" -> "application/gzip"

        // JSON/XML
        "json" -> "application/json"
        "xml" -> "application/xml"
        "html" -> "text/html"
        "css" -> "text/css"
        "js" -> "application/javascript"

        else -> "application/octet-stream"
    }
}

