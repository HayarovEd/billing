package com.impuls.chat.domain.utils

import android.util.Patterns
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Connection
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.StatusMessage
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File
import kotlin.time.Instant

internal object Utils {
    fun convertToLocalDateTime (timestamp: String): LocalDateTime {
        val instant = Instant.parse(timestamp)
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun collectMessages(connections: List<Connection>): List<Message> {
        return connections.flatMap { connection ->
           connection.messages
        }.sortedByDescending { it.date }
    }

    fun mergeChatUpdates(existingChat: Chat, updateChat: Chat): Pair<Chat, List<Message>> {
        val newSupportMessages = mutableListOf<Message>()

        // Обновляем основные поля чата
        val mergedConnections = existingChat.connections.map { existingConnection ->
            // Ищем обновление для этого connection
            val updatedConnection = updateChat.connections.find { it.id == existingConnection.id }

            if (updatedConnection != null) {
                // Ищем новые сообщения, которых нет в текущем соединении
                val existingIds = existingConnection.messages.map { it.id }.toSet()
                val newlyAdded = updatedConnection.messages.filter { it.id !in existingIds }

                // Добавляем новые сообщения от поддержки в список
                newSupportMessages.addAll(newlyAdded.filter { it.isSupport })

                // Объединяем сообщения: существующие + новые (без дубликатов по id)
                val mergedMessages = mergeMessages(existingConnection.messages, updatedConnection.messages)

                // Создаем обновленный connection с объединенными сообщениями
                existingConnection.copy(
                    start = updatedConnection.start,
                    end = updatedConnection.end,
                    user = updatedConnection.user,
                    messages = mergedMessages
                )
            } else {
                // Если обновления нет - оставляем как есть
                existingConnection
            }
        }

        // Добавляем новые соединения, которых не было в существующем чате
        val newConnections = updateChat.connections
            .filter { updatedConnection ->
                existingChat.connections.none { it.id == updatedConnection.id }
            }

        newConnections.forEach { connection ->
            newSupportMessages.addAll(connection.messages.filter { it.isSupport })
        }

        val allConnections = mergedConnections + newConnections

        // Возвращаем обновленный чат и список новых сообщений поддержки
        val chat = existingChat.copy(
            name = updateChat.name,
            email = updateChat.email,
            phone = updateChat.phone,
            isOpen = updateChat.isOpen,
            isWork = updateChat.isWork,
            group = updateChat.group,
            connections = allConnections
        )

        return chat to newSupportMessages
    }

    fun mergeMessages(existingMessages: List<Message>, newMessages: List<Message>): List<Message> {
        // Используем Map для быстрого поиска существующих сообщений по id
        val existingMessageMap = existingMessages.associateBy { it.id }.toMutableMap()

        // Добавляем или обновляем сообщения
        newMessages.forEach { newMessage ->
            val existingMessage = existingMessageMap[newMessage.id]
            if (existingMessage == null) {
                existingMessageMap[newMessage.id] = newMessage
            } else if (existingMessage.statusMessage != StatusMessage.READED) {
                // Если сообщение уже существует, обновляем его, только если оно еще не помечено как READED
                existingMessageMap[newMessage.id] = newMessage
            }
        }

        // Сортируем по дате (опционально)
        return existingMessageMap.values.sortedBy { it.date }
    }

}

fun getMimeTypeExtended(context: Context, uri: Uri): String {
    if (uri.scheme == "content") {
        return context.contentResolver.getType(uri) ?: "application/octet-stream"
    }
    val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
    if (extension.isNotEmpty()) {
        return getMimeTypeFromExtension(extension)
    }
    return "application/octet-stream"
}

fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    result = cursor.getString(index)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/') ?: -1
        if (cut != -1) {
            result = result?.substring(cut + 1)
        }
    }
    return result ?: "unknown"
}

fun getMimeTypeExtended(file: File): String {
    return getMimeTypeFromExtension(file.extension)
}

private fun getMimeTypeFromExtension(extension: String): String {
    return when (extension.lowercase()) {
        // Изображения
        "jpg", "jpeg" -> "image/jpeg"
        "png" -> "image/png"
        "gif" -> "image/gif"
        "bmp" -> "image/bmp"
        "webp" -> "image/webp"
        "svg" -> "image/svg+xml"

        // Документы
        "pdf" -> "application/pdf"
        "doc" -> "application/msword"
        "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        "xls" -> "application/vnd.ms-excel"
        "ppt" -> "application/vnd.ms-powerpoint"
        "odt" -> "application/vnd.oasis.opendocument.text"
        "ods" -> "application/vnd.oasis.opendocument.spreadsheet"
        "odp" -> "application/vnd.oasis.opendocument.presentation"
        "rtf" -> "application/rtf"
        "txt" -> "text/plain"
        "csv" -> "text/csv"

        // Видео
        "mp4" -> "video/mp4"
        "avi" -> "video/x-msvideo"
        "mov" -> "video/quicktime"
        "wmv" -> "video/x-ms-wmv"
        "flv" -> "video/x-flv"
        "mkv" -> "video/x-matroska"
        "webm" -> "video/webm"

        // Аудио
        "mp3" -> "audio/mpeg"
        "wav" -> "audio/wav"
        "ogg" -> "audio/ogg"
        "aac" -> "audio/aac"
        "flac" -> "audio/flac"
        "m4a" -> "audio/mp4"


        else -> "application/octet-stream"
    }
}
