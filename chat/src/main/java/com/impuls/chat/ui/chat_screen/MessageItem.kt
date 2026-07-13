package com.impuls.chat.ui.chat_screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.chat.R
import com.impuls.chat.domain.model.BaseMessage
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.TmpMessage
import com.impuls.chat.ui.uikit.UiIconButton
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun MessageItem(
    message: BaseMessage,
    token: String,
    onAction: (ChatScreenAction) -> Unit
) {
    LaunchedEffect(message.id, message.statusMessage) {
        if (message.isSupport && message.statusMessage != StatusMessage.READED) {
            onAction(ChatScreenAction.SetMessageAsRead(message.id))
        }
    }
    val alignment = if (message.isSupport) Alignment.Start else Alignment.End
    val backgroundColor = if (message.isSupport) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val textColor = if (message.isSupport) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isSupport) 0.dp else 16.dp,
                        bottomEnd = if (message.isSupport) 16.dp else 0.dp
                    )
                )
                .background(backgroundColor)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                if (message is Message) {
                    if (message.files.isNotEmpty()) {
                        MessageFiles(
                            files = message.files,
                            token = token,
                            onAction = onAction
                        )
                        if (message.text.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                if (message is TmpMessage) {
                    if (message.urisFile.isNotEmpty()) {
                        TmpMessageFiles(
                            files = message.urisFile,
                            token = token,
                            onAction = onAction
                        )
                        if (message.text.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                if (message.text.isNotEmpty()) {
                    Text(text = message.text, color = textColor)
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (!message.isSupport) {
                        StatusIndicator(status = message.statusMessage)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = message.date.toString().substring(11, 16),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun MessageFiles(
    token: String,
    files: List<MessageFile>,
    onAction: (ChatScreenAction) -> Unit
) {
    FlowRow(
        //modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 3
    ) {
        files.forEach { file ->
            Box(
                modifier = Modifier.clip(RoundedCornerShape(8.dp))
            ) {
                FileItem(
                    file = file,
                    token = token,
                    onAction = onAction
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(bottomStart = 36.dp)
                        )
                        .padding(start = 4.dp, bottom = 4.dp)
                ) {
                    UiIconButton(
                        modifier = Modifier.size(36.dp),
                        onClick = {
                            onAction(
                                ChatScreenAction.SaveFileLocal(
                                    fileName = file.fileUrl,
                                    originalName = file.originalName
                                )
                            )
                        },
                        icon = ImageVector.vectorResource(R.drawable.download_24dp),
                        size = 24.dp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun TmpMessageFiles(
    token: String,
    files: List<Uri>,
    onAction: (ChatScreenAction) -> Unit
) {
    FlowRow(
        //modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 3
    ) {
        files.forEach { file ->
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                TmpFileItem(
                    fileUri = file,
                    token = token,
                    onAction = onAction
                )
            }

        }
    }
}



@Composable
private fun StatusIndicator(status: StatusMessage) {
    val text = when (status) {
        StatusMessage.SENDED -> "..."
        StatusMessage.DELIVERED -> "✓"
        StatusMessage.ERROR_SEND -> "!"
        StatusMessage.READED -> "✓✓"
    }
    Text(
        text = text,
        fontSize = 10.sp,
        modifier = Modifier.padding(top = 2.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Preview(showBackground = true)
@Composable
private fun MessageItemUserPreview() {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    MaterialTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            MessageItem(
                message = Message(
                    date = now,
                    id = "1",
                    isSupport = false,
                    text = "Здравствуйте, у меня вопрос по оплате.",
                    statusMessage = StatusMessage.DELIVERED,
                    files = listOf(
                        MessageFile(
                            fileSize = 1024,
                            fileUrl = "https://example.com/image.jpg",
                            mimeType = MimeTypes.Image.JPEG,
                            originalName = "image.jpg"
                        )
                    )
                ),
                token = "",
                onAction = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageItemSupportPreview() {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    MaterialTheme {
        Box(modifier = Modifier.padding(8.dp)) {
            MessageItem(
                message = Message(
                    date = now,
                    id = "2",
                    isSupport = true,
                    text = "Добрый день! Слушаю вас.",
                    statusMessage = StatusMessage.READED,
                    files = emptyList()
                ),
                token = "",
                onAction = {}
            )
        }
    }
}
