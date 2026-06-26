package com.impuls.chat.domain.repository

interface NotificationRepository {
    fun showNotification(title: String, message: String, chatId: String? = null)
}
