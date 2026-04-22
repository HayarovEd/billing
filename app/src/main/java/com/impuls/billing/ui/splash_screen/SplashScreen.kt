package com.impuls.billing.ui.splash_screen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreenRoot(
    viewModel: SplashScreenViewModel = koinViewModel(),
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {


    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SplashScreenEvent.LoginNavigateEvent -> onNavigateToLogin()
            SplashScreenEvent.ProfileNavigateEvent -> onNavigateToProfile()
        }
    }

    SplashScreen()
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 2f,
                animationSpec = infiniteRepeatable(tween(2000)),
                label = ""
            )
            Image(
                modifier = modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin.Center
                    }
                    .align(Alignment.Center),
                painter = painterResource(R.drawable.logo_w),
                contentDescription = ""
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        SplashScreen()
    }
}

@Preview
@Composable
private fun Preview1() {
    BillingTheme(
        darkTheme = true
    ) {
        SplashScreen()
    }
}