package com.impuls.billing.ui.settings_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R.string
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun UpdateCard(
    modifier: Modifier = Modifier,
    isUpdating: Boolean,
    percentUpdate: Int,
    currentVersion: Double?,
    releaseVersion: Double,
    onUpdateApk: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            currentVersion?.let {
                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    text = "${stringResource(string.current_version)}: $currentVersion",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = modifier.height(6.dp))
            }
            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = stringResource(string.enable_update),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = modifier.height(6.dp))
            Button(
                onClick = onUpdateApk,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isUpdating,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = if (isUpdating) "${percentUpdate}%" else "${stringResource(string.update_until)} $releaseVersion",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        UpdateCard(
            isUpdating = false,
            percentUpdate = 0,
            currentVersion = 1.0,
            onUpdateApk = {},
            releaseVersion = 2.0
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    BillingTheme {
        UpdateCard(
            isUpdating = true,
            percentUpdate = 55,
            currentVersion = 1.0,
            onUpdateApk = {},
            releaseVersion = 2.0
        )
    }
}