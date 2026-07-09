package com.impuls.chat.ui.chat_screen

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.bhuvaneshw.pdf.PdfViewer
import com.bhuvaneshw.pdf.resource.DefaultNetworkResourceHandler
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
    val url = remember(file.fileUrl) { BASE_CHAT_URL + "files/" + file.fileUrl }

    when (val mime = file.mimeType) {
        is MimeTypes.Image -> {
            val imageRequest = remember(url, token, mime) {
                val headers = NetworkHeaders.Builder()
                    .set("Authorization", "Bearer $token")
                    .set("Content-Type", mime.mimeType)
                    .build()

                ImageRequest.Builder(context)
                    .data(url)
                    .apply {
                        if (mime == MimeTypes.Image.SVG_XML) {
                            decoderFactory(SvgDecoder.Factory())
                        }
                    }
                    .httpHeaders(headers)
                    .build()
            }

            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onAction(ChatScreenAction.ViewMedia(file)) },
                contentScale = ContentScale.Fit
            )
        }

        is MimeTypes.Audio -> {
            // Audio placeholder
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
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent))
            }
        }

        is MimeTypes.MimeType -> {
            Row(
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (mime == MimeTypes.MimeType.PDF) {
                    val networkResourceHandler = remember(token) {
                        DefaultNetworkResourceHandler(
                            beforeConnect = {
                                setRequestProperty("Authorization", "Bearer $token")
                            }
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .weight(1f)
                            .background(Color.White)
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                PdfViewer(ctx).apply {
                                    this.networkResourceHandler = networkResourceHandler
                                    this.onReady {
                                        this.ui.toolbarEnabled = false
                                        this.ui.viewerScrollbar = false
                                        this.pageAlignMode = PdfViewer.PageAlignMode.DEFAULT
                                        this.zoomTo(PdfViewer.Zoom.PAGE_FIT)
                                        this.load(url)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            onRelease = { it.reInitialize() }
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { onAction(ChatScreenAction.ViewMedia(file)) }
                        )
                    }
                } else {
                    Text(
                        text = remember(file.fileUrl) { "Файл: ${file.fileUrl.substringAfterLast("/")}" },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                        maxLines = 1
                    )
                    IconButton(onClick = {
                        onAction(ChatScreenAction.OpenFileAnotherApp(
                            fileName = file.fileUrl,
                            mimeType = mime
                        ))
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

        null -> {
            // Unknown file type
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
