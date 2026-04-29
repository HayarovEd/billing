package com.impuls.billing.ui.uikit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun ProfileRow(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    styleTitle: TextStyle = MaterialTheme.typography.bodySmall,
    styleContent: TextStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            text = "${title}:",
            style = styleTitle
        )
        Text(
            text = content,
            style = styleContent
        )
    }
}

@Composable
fun ProfileRowWithSave(
    modifier: Modifier = Modifier,
    title: String,
    count: Int,
    description: String,
    subTitle: String,
    content: String,
    defaultContent: String,
    isConfirmed: Boolean,
    isShowDialog: Boolean,
    onConfirmContent: (String) -> Unit,
    onConfirmCode: (String) -> Unit,
    onRecentCode: () -> Unit,
    updateVisibleDialog: (Boolean) -> Unit,
) {
    var currentContent by retain { mutableStateOf(content) }
    var currentCode by retain { mutableStateOf("") }
    var isShowRepeatSendCode by remember { mutableStateOf(false) }
  //  var isShowDialog by retain { mutableStateOf(false) }


    if (isShowDialog) {
        Dialog(
            onDismissRequest = {
                updateVisibleDialog(false)
                isShowRepeatSendCode = false
            }
        ) {
            ContentDialogSetContent(
                description = description,
                title = subTitle,
                content = currentContent,
                code = currentCode,
                count = count,
                isShowRepeatSendCode = isShowRepeatSendCode,
                onUpdateContent = {
                    currentContent = it
                },
                onUpdateCode = {
                    currentCode = it
                },
                onCancel = {
                    updateVisibleDialog(false)
                    isShowRepeatSendCode = false
                },
                onRecentCode = onRecentCode,
                onConfirmContent = {
                    onConfirmContent(currentContent)
                    isShowRepeatSendCode = true
                },
                onConfirmCode = {
                    onConfirmCode(currentCode)
                }
            )
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${title}:",
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier.clickable(
                    onClick = {
                        updateVisibleDialog(true)
                    }
                ),
                text = content.ifBlank { defaultContent },
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary,
                textDecoration = TextDecoration.Underline
            )
            if (content.isNotBlank() && !isConfirmed) {
                Spacer(modifier = modifier.width(3.dp))
                Text(
                    text = stringResource(R.string.not_confirm),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    BillingTheme {
        ProfileRow(
            title = "Контактный телефон",
            content = "555-5555"
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview1() {
    BillingTheme {
        ProfileRowWithSave(
            title = "Контактный телефон",
            description = stringResource(R.string.desc_phone),
            content = "555-5555",
            count = 0,
            isShowDialog = false,
            defaultContent = "Установить телефон",
            subTitle = stringResource(R.string.phone_number),
            isConfirmed = true,
            onConfirmContent = {},
            onConfirmCode = {},
            onRecentCode = {},
            updateVisibleDialog = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview2() {
    BillingTheme {
        ProfileRowWithSave(
            title = "Контактный телефон",
            content = "555-5555",
            count = 0,
            isShowDialog = false,
            defaultContent = "Установить телефон",
            description = stringResource(R.string.desc_phone),
            subTitle = stringResource(R.string.phone_number),
            isConfirmed = false,
            onConfirmContent = {},
            onConfirmCode = {},
            onRecentCode = {},
            updateVisibleDialog = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview1n() {
    BillingTheme(
        darkTheme = true
    ) {
        ProfileRowWithSave(
            title = "Контактный телефон",
            content = "",
            count = 0,
            isShowDialog = false,
            defaultContent = "Установить телефон",
            description = stringResource(R.string.desc_phone),
            subTitle = stringResource(R.string.phone_number),
            isConfirmed = false,
            onConfirmContent = {},
            onConfirmCode = {},
            onRecentCode = {},
            updateVisibleDialog = {}
        )
    }
}