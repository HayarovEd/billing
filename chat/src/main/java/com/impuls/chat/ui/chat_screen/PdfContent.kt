package com.impuls.chat.ui.chat_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bhuvaneshw.pdf.PdfViewer
import com.bhuvaneshw.pdf.resource.DefaultNetworkResourceHandler

@Composable
fun PdfContent(
    url: String,
    token: String,
    modifier: Modifier = Modifier,
    onAction: () -> Unit
) {
    val networkResourceHandler = remember(token) {
        DefaultNetworkResourceHandler(
            beforeConnect = {
                setRequestProperty("Authorization", "Bearer $token")
            }
        )
    }
    Box(
        modifier = modifier
            .height(200.dp)
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
                .clickable { onAction() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PdfContentPreview() {
    MaterialTheme {
        PdfContent(
            url = "https://example.com/sample.pdf",
            token = "fake_token",
            onAction = {}
        )
    }
}
