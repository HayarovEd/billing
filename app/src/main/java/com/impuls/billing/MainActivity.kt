package com.impuls.billing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.impuls.billing.ui.login_screen.LoginScreenRoot
import com.impuls.billing.ui.theme.BillingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BillingTheme {
                LoginScreenRoot()
            }
        }
    }
}