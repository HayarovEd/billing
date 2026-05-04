package com.impuls.billing.ui.uikit

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import androidx.core.graphics.toColorInt
import androidx.core.net.toUri
import com.impuls.billing.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    kwy2: Any? = null,
    onEvent: suspend (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(
        lifecycleOwner,
        key1,
        kwy2
    ) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}

fun String.toComposeColor(): Color {
    val hex = this.removePrefix("#")
    return if (hex.length == 6) {
        Color("#$hex".toColorInt())
    } else {
        Color("#$hex".toColorInt())
    }
}

fun openSbpUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        android.widget.Toast.makeText(
            context,
            "Не удалось открыть ссылку",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun PortraitOnly() {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    DisposableEffect(configuration) {
        val activity = context as? android.app.Activity
        val originalOrientation = activity?.requestedOrientation

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        onDispose {
            originalOrientation?.let { activity?.requestedOrientation = it }
        }
    }
}

fun LocalDate.toDebitUi(context: Context): String {
    val months = context.resources.getStringArray(R.array.months_nominative)
    val monthName = months[month.number - 1]
    return "$monthName ${this.year}"
}

fun LocalDate.toDebitUi2(context: Context): String {
    val monthNames = context.resources.getStringArray(R.array.month_names_genitive)
    val monthName = monthNames[month.number - 1]
    return "${this.day} $monthName ${this.year} ${context.resources.getString(R.string.of_year)}"
}
