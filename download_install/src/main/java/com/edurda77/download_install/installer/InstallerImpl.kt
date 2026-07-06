package com.edurda77.download_install.installer

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.edurda77.download_install.utils.pathToDownloadFile
import java.io.File

internal class InstallerImpl(
    private val application: Application
): Installer {

    override fun installAPK(downloadedFileName: String) {
        val file = File(application.pathToDownloadFile(downloadedFileName))
        if (file.exists()) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(
                    FileProvider.getUriForFile(
                        application,
                        "${application.packageName}.provider",
                        file
                    ),
                    "application/vnd.android.package-archive"
                )
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            application.startActivity(intent)
        } else {
            Log.e("REST TELE TV", "File does not exist: $downloadedFileName")
        }
    }
}