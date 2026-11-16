package com.impuls.chat.ui.chat_screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.utils.BASE_CHAT_URL
import com.impuls.chat.domain.utils.getFileName
import com.impuls.chat.domain.utils.getMimeTypeExtended
import com.impuls.chat.ui.chat_screen.ChatScreenAction.OpenFileAnotherApp
import com.impuls.chat.ui.chat_screen.ChatScreenAction.ViewMedia
import com.impuls.chat.ui.uikit.toImage

@Composable
fun FileItem(
    //modifier: Modifier = Modifier,
    file: MessageFile,
    token: String, onAction: (ChatScreenAction) -> Unit
) {
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
                    .clickable { onAction(ViewMedia(file)) },
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
                    .clickable { onAction(ViewMedia(file)) }
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (mime) {
                    MimeTypes.MimeType.PDF -> {
                        PdfContent(
                            modifier = Modifier,
                            url = url,
                            token = token,
                            onAction = {
                                onAction(ViewMedia(file))
                            }
                        )
                    }

                    else -> {
                        Text(
                            text = "Файл: ${file.originalName}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                        TextButton (
                            onClick = {
                                onAction(
                                    OpenFileAnotherApp(
                                        fileName = file.fileUrl,
                                        originalName = file.originalName,
                                        mimeType = mime
                                    )
                                )
                            }) {
                            Image(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(id = mime.toImage()),
                                contentDescription = "Open file",
                            )
                        }
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
fun TmpFileItem(
    //modifier: Modifier = Modifier,
    fileUri: Uri,
    token: String, onAction: (ChatScreenAction) -> Unit
) {
    val context = LocalContext.current
   val mimeType = getMimeTypeExtended(context, fileUri)

    when (val mime = MimeTypes.convertToMimeType(mimeType)) {
        is MimeTypes.Image -> {


            AsyncImage(
                model = fileUri,
                contentDescription = null,
                modifier = Modifier
                    .widthIn(max = 200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                   // .clickable { onAction(ViewMedia(fileUri)) },
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
                   // .clickable { onAction(ViewMedia(fileUri)) }
            ) {
                VideoPlayer(
                    url = fileUri.toString(),
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (mime) {
                    MimeTypes.MimeType.PDF -> {
                        PdfContent(
                            modifier = Modifier,
                            url = fileUri.toString(),
                            token = token,
                            onAction = {
                                //onAction(ViewMedia(fileUri))
                            }
                        )
                    }

                    else -> {
                        Text(
                            text = "Файл: ${getFileName(context, fileUri)}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                        Image(
                            modifier = Modifier.size(32.dp),
                            painter = painterResource(id = mime.toImage()),
                            contentDescription = "Open file",
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

@Preview(showBackground = true)
@Composable
private fun FileItemImagePreview() {
    MaterialTheme {
        FileItem(
            file = MessageFile(
                fileSize = 1024,
                fileUrl = "example.jpg",
                originalName = "image.jpg",
                mimeType = MimeTypes.Image.JPEG
            ),
            token = "",
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FileItemPdfPreview() {
    MaterialTheme {
        FileItem(
            file = MessageFile(
                fileSize = 1024,
                fileUrl = "example.pdf",
                originalName = "document.pdf",
                mimeType = MimeTypes.MimeType.PDF
            ),
            token = "",
            onAction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FileItemDocPreview() {
    MaterialTheme {
        FileItem(
            file = MessageFile(
                fileSize = 1024,
                fileUrl = "example.docx",
                originalName = "document.docx",
                mimeType = MimeTypes.MimeType.MS_WORD_OPEN_XML
            ),
            token = "",
            onAction = {}
        )
    }
}
