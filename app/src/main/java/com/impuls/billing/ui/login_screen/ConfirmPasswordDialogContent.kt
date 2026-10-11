package com.impuls.billing.ui.login_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.UiIconButton
import com.impuls.billing.ui.uikit.UiTextField

@Composable
fun ConfirmPasswordDialogContent(
    modifier: Modifier = Modifier,
    count: Int,
    destination: String,
    type: RecoverType,
    onCloseClick: () -> Unit,
    onRecentClick: () -> Unit,
    onConfirmClick: (String, String) -> Unit,
) {
    var code by retain { mutableStateOf("") }
    var password by retain { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onBackground
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(2.dp),
        ) {
            UiIconButton(
                modifier = modifier.align(alignment = Alignment.TopEnd),
                icon = ImageVector.vectorResource(R.drawable.close_24dp),
                onClick = onCloseClick
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(
                        R.string.recover_password_code,
                        "${stringResource(type.descriptionRes)} $destination"
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = modifier.height(5.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier,
                        text = "${stringResource(R.string.enter_code)}:",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = modifier.width(5.dp))
                    UiTextField(
                        content = code,
                        label = "",
                        onClickContent = { code = it }
                    )
                }
                Spacer(modifier = modifier.height(5.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier,
                        text = "${stringResource(R.string.enter_new_password)}:",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = modifier.width(5.dp))
                    UiTextField(
                        content = password,
                        label = "",
                        onClickContent = { password = it }
                    )
                }
                Spacer(modifier = modifier.height(5.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        modifier = modifier.weight(1f),
                        onClick = { onConfirmClick(code, password) },
                        enabled = code.isNotBlank()&&password.isNotBlank(),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = colorScheme.onBackground
                        )
                    ) {
                        Text(
                            modifier = modifier.basicMarquee(),
                            text = stringResource(R.string.recover_password),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                        )
                    }
                    Spacer(modifier = modifier.width(5.dp))
                    OutlinedButton(
                        modifier = modifier.weight(1f),
                        enabled = count == 0,
                        onClick = { onRecentClick() },
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            width = 1.dp,
                            color = colorScheme.onBackground
                        )
                    ) {
                        val text = if (count == 0) stringResource(R.string.recend_code) else "${
                            stringResource(R.string.recend_code)
                        }($count)"
                        Text(
                            modifier = modifier.basicMarquee(),
                            text = text,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
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
        ConfirmPasswordDialogContent(
            destination = "ed@ram.ty",
            count = 0,
            type = RecoverType.EMAIL,
            onCloseClick = {},
            onConfirmClick = {_,_->},
            onRecentClick = {},
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview1() {
    BillingTheme {
        ConfirmPasswordDialogContent(
            destination = "4556557577",
            count = 5,
            type = RecoverType.SMS,
            onCloseClick = {},
            onConfirmClick = {_,_->},
            onRecentClick = {},
        )
    }
}