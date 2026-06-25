package com.impuls.chat.domain.utils

import android.util.Patterns
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Connection
import com.impuls.chat.domain.model.Message
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

internal object Utils {
    fun convertToLocalDateTime (timestamp: String): LocalDateTime {
        val instant = Instant.parse(timestamp)
        return instant.toLocalDateTime(TimeZone.currentSystemDefault())
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun formatLocalDateTime(dateTime: LocalDateTime): String {
        return "${dateTime.day}-${dateTime.month.number}-${dateTime.year} ${dateTime.hour}:${dateTime.minute}"
    }

    fun main() {
        val dateTime = LocalDateTime(2026, 6, 23, 8, 45)
        println(formatLocalDateTime(dateTime)) // 23 июня 2026 года 08:45
    }

    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun collectMessages(connections: List<Connection>): List<Message> {
        return connections.flatMap { connection ->
           connection.messages
        }.sortedByDescending { it.date }
    }

    fun mergeChatUpdates(existingChat: Chat, updateChat: Chat): Chat {
        // Обновляем основные поля чата
        val mergedConnections = existingChat.connections.map { existingConnection ->
            // Ищем обновление для этого connection
            val updatedConnection = updateChat.connections.find { it.id == existingConnection.id }

            if (updatedConnection != null) {
                // Объединяем сообщения: существующие + новые (без дубликатов по id)
                val mergedMessages = mergeMessages(existingConnection.messages, updatedConnection.messages)

                // Создаем обновленный connection с объединенными сообщениями
                existingConnection.copy(
                    start = updatedConnection.start, // или оставить существующий, если нужно
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

        val allConnections = mergedConnections + newConnections

        // Возвращаем обновленный чат
        return existingChat.copy(
            name = updateChat.name,
            email = updateChat.email,
            phone = updateChat.phone,
            isOpen = updateChat.isOpen,
            isWork = updateChat.isWork,
            subgroup = updateChat.subgroup,
            connections = allConnections
        )
    }

    fun mergeMessages(existingMessages: List<Message>, newMessages: List<Message>): List<Message> {
        // Используем Map для быстрого поиска существующих сообщений по id
        val existingMessageMap = existingMessages.associateBy { it.id }.toMutableMap()

        // Добавляем новые сообщения (если id нет в существующих)
        newMessages.forEach { newMessage ->
            if (!existingMessageMap.containsKey(newMessage.id)) {
                existingMessageMap[newMessage.id] = newMessage
            }
            // Если id уже существует - можно либо пропустить, либо обновить (зависит от логики)
            // В данном случае пропускаем, чтобы сохранить существующие данные
        }

        // Сортируем по дате (опционально)
        return existingMessageMap.values.sortedBy { it.date }
    }

}