package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.UiTextField

@Composable
fun ContentDialogSetContent(
    modifier: Modifier = Modifier,
    decription: String,
    title: String,
    content: String,
    code: String,
    count: Int,
    isShowRepeatSendCode: Boolean,
    onUpdateContent: (String) -> Unit,
    onUpdateCode: (String) -> Unit,
    onConfirmContent: () -> Unit,
    onConfirmCode: () -> Unit,
    onRecendCode: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = decription,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = modifier.height(5.dp))
            if (isShowRepeatSendCode) {
                RowWithTextField(
                    title = stringResource(R.string.code_confirm),
                    content = code,
                    onUpdateContent = { onUpdateCode(it) }
                )
            } else {
                RowWithTextField(
                    title = title,
                    content = content,
                    onUpdateContent = { onUpdateContent(it) }
                )
            }
            Spacer(modifier = modifier.height(5.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(
                    modifier = modifier.weight(1f),
                    onClick = onCancel,
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = modifier.width(3.dp))

                OutlinedButton(
                    modifier = modifier.weight(2f),
                    onClick =  if (isShowRepeatSendCode) onConfirmCode else onConfirmContent,
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = if (isShowRepeatSendCode) stringResource(R.string.confirm) else stringResource(R.string.save),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(modifier = modifier.height(5.dp))
            if (isShowRepeatSendCode) {
                OutlinedButton(
                    onClick = onRecendCode,
                    enabled = count == 0,
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    val buttonText =
                        if (count == 0) stringResource(R.string.send_repeate_code) else "${
                            stringResource(R.string.send_repeate_code)
                        }($count)"
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun RowWithTextField(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onUpdateContent: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "${title}:",
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = modifier.width(3.dp))
        UiTextField(
            content = content,
            label = "",
            onClickContent = { onUpdateContent(it) }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        ContentDialogSetContent(
            decription = "Если у Вас изменился номер, пожалуйста, укажите его в соответствующем поле.",
            content = "88888",
            code = "",
            count = 8,
            isShowRepeatSendCode = false,
            title = "Номер телефона",
            onUpdateCode = {},
            onUpdateContent = {},
            onCancel = {},
            onConfirmCode = {},
            onConfirmContent = {},
            onRecendCode = {},
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    BillingTheme {
        ContentDialogSetContent(
            decription = "Если у Вас изменился номер, пожалуйста, укажите его в соответствующем поле.",
            content = "88888",
            code = "5655",
            count = 8,
            isShowRepeatSendCode = true,
            title = "Номер телефона",
            onUpdateCode = {},
            onUpdateContent = {},
            onCancel = {},
            onConfirmCode = {},
            onConfirmContent = {},
            onRecendCode = {},
        )
    }
}

@Preview
@Composable
private fun Preview3() {
    BillingTheme {
        ContentDialogSetContent(
            decription = "Если у Вас изменился номер, пожалуйста, укажите его в соответствующем поле.",
            content = "88888",
            code = "",
            count = 0,
            isShowRepeatSendCode = true,
            title = "Номер телефона",
            onUpdateCode = {},
            onUpdateContent = {},
            onCancel = {},
            onConfirmCode = {},
            onConfirmContent = {},
            onRecendCode = {},
        )
    }
}