package com.impuls.billing.ui.payment_screen

import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.UiIconButton

@Composable
fun PaymentCardUi(
    modifier: Modifier = Modifier,
    url: String?,
    onBack: () -> Unit,
) {



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        UiIconButton(
            icon = ImageVector.vectorResource(R.drawable.arrow_back_24dp),
            onClick = onBack,
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.add_balance),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        // Описание
        Text(
            text = stringResource(R.string.add_balance_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.pay_to_card),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = modifier.height(10.dp))
        url?.let {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        // Обязательно задаем стандартные клиенты Chromium
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()

                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true // Важно для localStorage шлюза
                            databaseEnabled = true
                            allowFileAccess = true

                            // Разрешаем Cross-Origin для локальных оберток
                            allowUniversalAccessFromFileURLs = true
                            allowFileAccessFromFileURLs = true

                            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        }

                        // Включаем Cookie строго внутри factory
                        val cookieManager = CookieManager.getInstance()
                        cookieManager.setAcceptCookie(true)
                        cookieManager.setAcceptThirdPartyCookies(this, true)
                    }
                },
                update = { webView ->
                    if (webView.url != url) {
                        // КРИТИЧЕСКИЙ ШАГ: Подменяем заголовки шлюза acq.bisys.ru,
                        // чтобы фреймы внутри WebView распознали Origin как доверенный
                        val extraHeaders = mapOf(
                            "Referer" to "https://acq.bisys.ru/",
                            "Origin" to "https://acq.bisys.ru"
                        )
                        webView.loadUrl(url, extraHeaders)
                    }
                },
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        PaymentCardUi(
            url = null,
            onBack = {}
        )
    }
}