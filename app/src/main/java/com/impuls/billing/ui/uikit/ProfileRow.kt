package com.impuls.billing.ui.uikit

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
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun ProfileRow(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            text = "${title}:",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = content,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun ProfileRowWithSave(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    defaultContent: String,
    isConfirmed: Boolean,
    onClickSave: (String) -> Unit,
    onConfirm: (String) -> Unit,
) {
    var currentContent by retain { mutableStateOf(content) }

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
            content = "555-5555",
            defaultContent = "Установить телефон",
            isConfirmed = true,
            onClickSave = {},
            onConfirm = {}
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
            defaultContent = "Установить телефон",
            isConfirmed = false,
            onClickSave = {},
            onConfirm = {}
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
            defaultContent = "Установить телефон",
            isConfirmed = false,
            onClickSave = {},
            onConfirm = {}
        )
    }
}