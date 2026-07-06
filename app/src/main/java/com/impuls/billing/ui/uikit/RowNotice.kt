package com.impuls.billing.ui.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.ui.settings_screen.TypeNotice
import com.impuls.billing.ui.theme.BillingTheme

private val CheckboxColumnWidth = 48.dp
private val VolumeFieldWidth = 60.dp
private val VolumeFieldPadding = 8.dp

@Composable
fun RowNotice(
    modifier: Modifier = Modifier,
    typeNotice: TypeNotice,
    isCheckedSms: Boolean,
    isCheckedEmail: Boolean,
    enableSms: Boolean,
    enableEmail: Boolean,
    updateCheckSms: (Boolean) -> Unit,
    updateCheckEmail: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = typeNotice.asTypeNoticeText().asString(),
            style = MaterialTheme.typography.bodySmall,
        )
        
        // Placeholder for the volume field to keep checkboxes aligned with RowNoticeWithVolume
        Spacer(modifier = Modifier.width(VolumeFieldWidth + VolumeFieldPadding))
        
        NoticeCheckbox(
            checked = isCheckedSms,
            enabled = enableSms,
            onCheckedChange = updateCheckSms
        )
        
        NoticeCheckbox(
            checked = isCheckedEmail,
            enabled = enableEmail,
            onCheckedChange = updateCheckEmail
        )
    }
}

@Composable
fun RowNoticeOnlyEmail(
    modifier: Modifier = Modifier,
    typeNotice: TypeNotice,
    isCheckedEmail: Boolean,
    enableEmail: Boolean,
    updateCheckEmail: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = typeNotice.asTypeNoticeText().asString(),
            style = MaterialTheme.typography.bodySmall,
        )
        
        // Placeholder for Volume Field
        Spacer(modifier = Modifier.width(VolumeFieldWidth + VolumeFieldPadding))
        
        // Placeholder for SMS column
        Spacer(modifier = Modifier.width(CheckboxColumnWidth))
        
        NoticeCheckbox(
            checked = isCheckedEmail,
            enabled = enableEmail,
            onCheckedChange = updateCheckEmail
        )
    }
}

@Composable
fun RowNoticeWithVolume(
    modifier: Modifier = Modifier,
    volume: String,
    typeNotice: TypeNotice,
    isCheckedSms: Boolean,
    isCheckedEmail: Boolean,
    enableSms: Boolean,
    enableEmail: Boolean,
    updateCheckSms: (Boolean) -> Unit,
    updateCheckEmail: (Boolean) -> Unit,
    updateVolume: (String) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = typeNotice.asTypeNoticeText().asString(),
            style = MaterialTheme.typography.bodySmall,
        )
        
        UiTextField3(
            modifier = Modifier.width(VolumeFieldWidth),
            content = volume,
            onClickContent = updateVolume,
            isOnlyDigit = true,
            keyboardType = KeyboardType.Number,
            label = ""
        )
        
        Spacer(modifier = Modifier.width(VolumeFieldPadding))
        
        NoticeCheckbox(
            checked = isCheckedSms,
            enabled = enableSms,
            onCheckedChange = updateCheckSms
        )
        
        NoticeCheckbox(
            checked = isCheckedEmail,
            enabled = enableEmail,
            onCheckedChange = updateCheckEmail
        )
    }
}

@Composable
private fun NoticeCheckbox(
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.size(CheckboxColumnWidth),
        contentAlignment = Alignment.Center
    ) {
        Checkbox(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme() {
        RowNotice(
            typeNotice = TypeNotice.MONTHLY_REPORT_ACCOUNT_BALANCE,
            isCheckedSms = true,
            isCheckedEmail = false,
            enableSms = true,
            enableEmail = false,
            updateCheckSms = {},
            updateCheckEmail = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview1() {
    BillingTheme() {
        RowNoticeWithVolume(
            typeNotice = TypeNotice.WARNING_BALANCE,
            volume = "18",
            isCheckedSms = true,
            isCheckedEmail = false,
            enableSms = true,
            enableEmail = false,
            updateCheckSms = {},
            updateCheckEmail = {},
            updateVolume = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    BillingTheme() {
        RowNoticeOnlyEmail(
            typeNotice = TypeNotice.MONTHLY_REPORT,
            isCheckedEmail = false,
            enableEmail = false,
            updateCheckEmail = {},
        )
    }
}