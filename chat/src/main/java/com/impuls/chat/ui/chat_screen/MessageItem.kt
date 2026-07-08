package com.impuls.chat.ui.chat_screen

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.utils.BASE_CHAT_URL
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun MessageItem(
    message: Message,
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
                .padding(12.dp)
        ) {
            Column {
                if (message.files.isNotEmpty()) {
                    MessageFiles(
                        files = message.files,
                        token = token
                    )
                    if (message.text.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                if (message.text.isNotEmpty()) {
                    Text(text = message.text, color = textColor)
                }
                if (!message.isSupport) {
                    StatusIndicator(status = message.statusMessage)
                }
            }
        }
        Text(
            text = message.date.toString().substring(11, 16),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun MessageFiles(
    token: String,
    files: List<MessageFile>
) {
    FlowRow(
        //modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 3
    ) {
        files.forEach { file ->
            FileItem(
                file = file,
                token = token
            )
        }
    }
}

@Composable
private fun FileItem(file: MessageFile,  token: String) {
    val context = LocalContext.current
    val isImage = file.mimeType is MimeTypes.Image
    val url = BASE_CHAT_URL + "files/" + file.imageUrl
    val headers = NetworkHeaders.Builder()
        .set("Authorization", "Bearer $token")
        .set("Content-Type", file.mimeType?.mimeType?:"application/octet-stream")
        .build()
    //Log.d("check chat 1234", "url - $sss")
    if (isImage) {
        val model = if (file.mimeType == MimeTypes.Image.SVG_XML) {
            ImageRequest.Builder(context)
                .data(url)
                .decoderFactory(SvgDecoder.Factory())
                .httpHeaders(headers)
                .build()
        } else  {
            ImageRequest.Builder(context)
                .data(url)
                .httpHeaders(headers)
                .build()
        }
        AsyncImage(
           //model = BASE_CHAT_URL + "files/" + file.imageUrl,
            model = model,
            contentDescription = null,
            modifier = Modifier
                .widthIn(max = 200.dp)
                //.aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Файл: ${file.imageUrl.substringAfterLast("/")}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
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
                            imageUrl = "https://example.com/image.jpg",
                            mimeType = MimeTypes.Image.JPEG
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
