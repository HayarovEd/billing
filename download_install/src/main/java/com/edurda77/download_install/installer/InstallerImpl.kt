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

    override fun canRequestPackageInstalls(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            application.packageManager.canRequestPackageInstalls()
        } else {
            true
        }
    }

    override fun installAPK(downloadedFileName: String) {
        val file = File(application.pathToDownloadFile(downloadedFileName))
        if (file.exists()) {
            val authority = "${application.packageName}.provider"
            try {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(
                        FileProvider.getUriForFile(
                            application,
                            authority,
                            file
                        ),
                        "application/vnd.android.package-archive"
                    )
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                application.startActivity(intent)
            } catch (e: Exception) {
                Log.e("InstallerImpl", "Failed to start install activity: ${e.message}")
            }
        } else {
            Log.e("InstallerImpl", "File does not exist: $downloadedFileName")
        }
    }
}