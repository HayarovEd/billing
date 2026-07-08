package com.impuls.chat.ui.chat_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.core.net.toUri

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
                        token = token,
                        onAction = onAction
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
            FileItem(
                file = file,
                token = token,
                onAction = onAction
            )
        }
    }
}


@Composable
private fun FileItem(file: MessageFile, token: String, onAction: (ChatScreenAction) -> Unit) {
    val context = LocalContext.current
    val url = BASE_CHAT_URL + "files/" + file.fileUrl
    //Log.d("check chat 1234", "url - $sss")
    when (file.mimeType) {
        is MimeTypes.Image -> {
            val headers = NetworkHeaders.Builder()
                .set("Authorization", "Bearer $token")
                .set("Content-Type", file.mimeType.mimeType)
                .build()
            AsyncImage(
                //model = BASE_CHAT_URL + "files/" + file.imageUrl,
                model = ImageRequest.Builder(context)
                    .data(url)
                    .apply {
                        if (file.mimeType == MimeTypes.Image.SVG_XML) {
                            decoderFactory(SvgDecoder.Factory())
                        }
                    }
                    .httpHeaders(headers)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    //.aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onAction(ChatScreenAction.ViewMedia(file)) },
                contentScale = ContentScale.Fit
            )
        }

        is MimeTypes.Audio -> {

        }

        is MimeTypes.Video -> {
            Box(
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onAction(ChatScreenAction.ViewMedia(file)) }
            ) {
                VideoPlayer(
                    url = url,
                    token = token,
                    isUseController = false,
                    isPlayWhenReady = false,
                    modifier = Modifier.fillMaxSize()
                )
                // Overlay to catch clicks if VideoPlayer consumes them, 
                // or just to make the whole area clickable for full screen
                Box(modifier = Modifier.fillMaxSize().background(Color.Transparent))
            }
        }

        is MimeTypes.MimeType -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Файл: ${file.fileUrl.substringAfterLast("/")}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(file.fileUrl.toUri(), file.mimeType.mimeType)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(intent, "Открыть файл"))
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Open file",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
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
