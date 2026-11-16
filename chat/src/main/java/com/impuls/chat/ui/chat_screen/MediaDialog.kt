package com.impuls.chat.ui.chat_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.bhuvaneshw.pdf.compose.PdfSource
import com.bhuvaneshw.pdf.compose.PdfViewer
import com.bhuvaneshw.pdf.compose.rememberPdfState
import com.bhuvaneshw.pdf.resource.DefaultNetworkResourceHandler
import com.impuls.chat.R
import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.utils.BASE_CHAT_URL
import com.impuls.chat.ui.uikit.UiIconButton

@Composable
fun MediaDialog(
    file: MessageFile,
    token: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val url = remember(file.fileUrl) { BASE_CHAT_URL + "files/" + file.fileUrl }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
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
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                is MimeTypes.Video -> {
                    VideoPlayer(
                        url = url,
                        token = token,
                        isUseController = true,
                        isPlayWhenReady = false,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is MimeTypes.MimeType -> {
                    if (mime == MimeTypes.MimeType.PDF) {
                        val pdfState = rememberPdfState(
                            PdfSource.Url(url)
                        )
                        val networkResourceHandler = remember(token) {
                            DefaultNetworkResourceHandler(
                                beforeConnect = {
                                    setRequestProperty("Authorization", "Bearer $token")
                                }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .clickable(enabled = false) {}
                        ) {
                            PdfViewer(
                                pdfState = pdfState,
                                modifier = Modifier.fillMaxSize(),
                                onCreateViewer = {
                                    this.networkResourceHandler = networkResourceHandler
                                    this.onReady {
                                        this.ui.toolbarEnabled = true
                                        this.ui.viewerScrollbar = true
                                        this.zoomTo(com.bhuvaneshw.pdf.PdfViewer.Zoom.PAGE_FIT)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            UiIconButton(
                icon = ImageVector.vectorResource(R.drawable.close_24dp),
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun MediaDialogPreview() {
    MaterialTheme {
        MediaDialog(
            file = MessageFile(
                fileSize = 1024,
                fileUrl = "example.jpg",
                originalName = "image.jpg",
                mimeType = MimeTypes.Image.JPEG
            ),
            token = "dummy_token",
            onDismiss = {}
        )
    }
}
