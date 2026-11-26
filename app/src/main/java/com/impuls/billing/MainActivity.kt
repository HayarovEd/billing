package com.impuls.billing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.edurda77.download_install.model.UpdateConfig
import com.edurda77.download_install.refresher.Refresher
import com.impuls.billing.ui.navigation.NavController
import com.impuls.billing.ui.theme.BillingTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null
    private val refresher: Refresher by inject()
    private val updateConfig: UpdateConfig by inject()

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                refresher.checkUpdatesInBackground(updateConfig, forceNotify = false)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Test FCM", "onCreate: ${intent?.data}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        enableEdgeToEdge()
        setContent {
            BillingTheme(
                dynamicColor = false
            ) {
                val controller = rememberNavController()
                navController = controller
                NavController(navController = controller)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("Test FCM", "onNewIntent: ${intent.data}")
        setIntent(intent)
        navController?.handleDeepLink(intent)
    }
}
