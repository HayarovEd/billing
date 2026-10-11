package com.edurda77.download_install.utils

import android.content.Context

fun Context.pathToDownloadFile(fileName: String): String =
    "${externalCacheDir?.absolutePath}/$fileName"