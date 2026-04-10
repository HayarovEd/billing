package com.impuls.billing.ui.login_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.UiIconButton
import com.impuls.billing.ui.uikit.UiTextField

@Composable
fun ForgetPasswordDialogContent(
    modifier: Modifier = Modifier,
    login: String,
    recoverType: RecoverType,
    onCloseClick: () -> Unit,
    updateLogin: (String) -> Unit,
    updateRecoverType: (RecoverType) -> Unit,
    onRecoverClick: () -> Unit,
) {
    var isOpenMenu by retain { mutableStateOf(false) }
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onBackground
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
                modifier = modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.recover_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(5.dp))
            UiTextField(
                content = login,
                label = "${stringResource(R.string.login)}:",
                onClickContent = { updateLogin(it) }
            )
            Spacer(modifier = modifier.height(5.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.recover_from)}:",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = modifier.width(5.dp))
                Column() {
                    Text(
                        text = stringResource(recoverType.descriptionRes),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    DropdownMenu(
                        onDismissRequest = {
                            isOpenMenu = false
                        },
                        expanded = isOpenMenu
                    ) {
                        RecoverType.entries.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        modifier = modifier,
                                        text = stringResource(it.descriptionRes),
                                        color = colorScheme.onBackground,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                       },
                                onClick = {
                                    updateRecoverType(it)
                                    isOpenMenu = false
                                }
                            )
                        }
                    }
                }

                UiIconButton(
                    icon = if (isOpenMenu) ImageVector.vectorResource(R.drawable.arrow_drop_down_24dp) else  ImageVector.vectorResource(R.drawable.arrow_drop_up_24dp),
                    onClick = {
                        isOpenMenu = true
                    }
                )
            }
            Spacer(modifier = modifier.height(5.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    modifier = modifier.weight(2f),
                    onClick = onCloseClick,
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = modifier.width(5.dp))
                OutlinedButton(
                    modifier = modifier.weight(3f),
                    enabled = login.isNotBlank(),
                    onClick = { onRecoverClick() },
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(R.string.recover_password),
                        style = MaterialTheme.typography.labelSmall
                    )
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
        ForgetPasswordDialogContent(
            login = "",
            recoverType = RecoverType.SMS,
            onCloseClick = {},
            onRecoverClick = {},
            updateLogin = {},
            updateRecoverType = {}
        )
    }
}