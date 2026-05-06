package com.impuls.billing.ui.settings_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.impuls.billing.R.drawable
import com.impuls.billing.R.string
import com.impuls.billing.ui.uikit.ContentDialogSetContent
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun ContactsCard(
    modifier: Modifier = Modifier,
    mobile: String,
    mobileIsConfirmed: Boolean,
    emailIsConfirmed: Boolean,
    email: String,
    count: Int,
    onUpdatePhone: (String) -> Unit,
    onUpdateEmail: (String) -> Unit,
    onConfirmCodePhone: (String) -> Unit,
    onConfirmCodeEmail: (String) -> Unit,
    onRecentCodePhone: () -> Unit,
    onRecentCodeEmail: () -> Unit,
) {
    var visiblePhoneDialog by retain { mutableStateOf(false) }
    var visibleEmailDialog by retain { mutableStateOf(false) }
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
            InfoContactRow(
                icon = ImageVector.vectorResource(
                    drawable.mobile_check_24dp),
                label = stringResource(string.phone),
                labelButton = stringResource(string.change_mobile),
                value = mobile,
                isConfirmed = mobileIsConfirmed,
                description = stringResource(string.desc_phone),
                subTitle = stringResource(string.phone_number),
                defaultContent = stringResource(string.set_phone),
                isShowDialog = visiblePhoneDialog,
                count = count,
                onConfirmCode = {
                    onConfirmCodePhone(it)
                },
                onRecentCode = onRecentCodePhone,
                onConfirmContent = {
                    onUpdatePhone(it)
                },
                updateVisibleDialog = {
                    visiblePhoneDialog = it
                },
                onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
                successColor = MaterialTheme.colorScheme.primary,
                errorColor = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Телефон с верификацией
            InfoContactRow(
                icon = ImageVector.vectorResource(
                    drawable.alternate_email_24dp),
                label = stringResource(string.email),
                labelButton = stringResource(string.change_email),
                value = email,
                isConfirmed = emailIsConfirmed,
                description = stringResource(string.desc_email),
                subTitle = stringResource(string.email_adrress),
                defaultContent = stringResource(string.set_email),
                isShowDialog = visibleEmailDialog,
                count = count,
                onConfirmCode = {
                    onConfirmCodeEmail(it)
                },
                onRecentCode = onRecentCodeEmail,
                onConfirmContent = {
                    onUpdateEmail(it)
                },
                updateVisibleDialog = {
                    visibleEmailDialog = it
                },
                onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
                successColor = MaterialTheme.colorScheme.primary,
                errorColor = MaterialTheme.colorScheme.error
            )
        }
    }

}


@Composable
private fun InfoContactRow(
    icon: ImageVector,
    label: String,
    labelButton: String,
    value: String,
    isConfirmed: Boolean,
    count: Int,
    description: String,
    subTitle: String,
    defaultContent: String,
    isShowDialog: Boolean,
    onConfirmContent: (String) -> Unit,
    onConfirmCode: (String) -> Unit,
    onRecentCode: () -> Unit,
    updateVisibleDialog: (Boolean) -> Unit,
    onSurfaceVariant: Color,
    successColor: Color,
    errorColor: Color
) {
    var currentContent by retain { mutableStateOf(value) }
    var currentCode by retain { mutableStateOf("") }
    var isShowRepeatSendCode by remember { mutableStateOf(false) }

    if (isShowDialog) {
        Dialog(
            onDismissRequest = {
               /* updateVisibleDialog(false)
                isShowRepeatSendCode = false*/
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = onSurfaceVariant
                    )
                    Text(
                        text = value.ifBlank { defaultContent },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = if (label == "Телефон") FontFamily.Monospace else null
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    imageVector = if (isConfirmed) ImageVector.vectorResource(drawable.check_circle_24dp)
                    else ImageVector.vectorResource(drawable.close_24dp),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isConfirmed) successColor else errorColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { updateVisibleDialog(true) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = labelButton,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        ContactsCard(
            mobile = "8902665323",
            email = "edr@rqwt.er",
            mobileIsConfirmed = true,
            emailIsConfirmed = false,
            count = 5,
            onUpdateEmail = {},
            onUpdatePhone = {},
            onRecentCodeEmail = {},
            onRecentCodePhone = {},
            onConfirmCodeEmail = {},
            onConfirmCodePhone = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    BillingTheme {
        InfoContactRow(
            icon = ImageVector.vectorResource(
                drawable.alternate_email_24dp),
            label = stringResource(string.email),
            labelButton = stringResource(string.change_email),
            value = "edr@rqwt.er",
            isConfirmed = true,
            description = stringResource(string.desc_email),
            subTitle = stringResource(string.email_adrress),
            defaultContent = stringResource(string.set_email),
            isShowDialog = false,
            count = 4,
            onConfirmCode = {

            },
            onRecentCode = { },
            onConfirmContent = {},
            updateVisibleDialog = {},
            onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
            successColor = MaterialTheme.colorScheme.primary,
            errorColor = MaterialTheme.colorScheme.error
        )
    }
}