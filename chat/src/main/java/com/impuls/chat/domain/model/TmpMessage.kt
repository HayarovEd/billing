package com.impuls.chat.domain.model

import android.net.Uri
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class TmpMessage(
    override val date: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    override val id: String = "-1",
    override val isSupport: Boolean = false,
    override val text: String = "",
    override val statusMessage: StatusMessage = StatusMessage.SENDED,
    val urisFile: List<Uri>
): BaseMessage(
    date = date,
    id = id,
    isSupport = isSupport,
    text = text,
    statusMessage = statusMessage
)
