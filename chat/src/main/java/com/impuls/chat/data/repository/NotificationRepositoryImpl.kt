package com.impuls.chat.data.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.impuls.chat.domain.repository.NotificationRepository

internal class NotificationRepositoryImpl(
    private val context: Context
) : NotificationRepository {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        private const val CHANNEL_ID = "chat_notifications"
        private const val CHANNEL_NAME = "Chat Notifications"
        private const val DEFAULT_NOTIFICATION_ID = 0
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for new messages"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(
        title: String,
        message: String,
        chatId: String?,
        contentData: Map<String, String>?
    ) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "billing://profile/".toUri(),
            context,
            Class.forName("com.impuls.billing.MainActivity")
        ).apply {
            Log.d("NotificationRepo", "Creating intent with data: ${"billing://profile/".toUri()}")
            contentData?.forEach { (key, value) ->
                Log.e("Test FCM", "key: $key, value: $value")
                putExtra(key, value)
            }
            // Ensure the activity is opened even if it's already running
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val notificationId = chatId?.hashCode() ?: DEFAULT_NOTIFICATION_ID
        val requestCode = chatId?.hashCode() ?: DEFAULT_NOTIFICATION_ID

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(context.applicationInfo.icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true) // не звонить повторно при дублях
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}
