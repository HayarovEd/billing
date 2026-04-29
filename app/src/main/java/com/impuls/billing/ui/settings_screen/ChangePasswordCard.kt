package com.impuls.billing.ui.settings_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.impuls.billing.R
import com.impuls.billing.ui.uikit.ContentDialogUpdatePassword
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun ChangePasswordCard(
    modifier: Modifier = Modifier,
    isVisibleDialog: Boolean,
    isUpdatingPassword: Boolean,
    updateVisibilityDialog: (Boolean) -> Unit,
    onUpdatePassword: (old: String, new: String, confirm: String) -> Unit,
    onClickLogoff: () -> Unit,
) {
    var currentPassword by retain { mutableStateOf("") }
    var newPassword by retain { mutableStateOf("") }
    var confirmPassword by retain { mutableStateOf("") }

    if (isVisibleDialog) {
        Dialog(
            onDismissRequest = {
                updateVisibilityDialog(false)
            },
            content = {
                ContentDialogUpdatePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword,
                    isUpdatingPassword = isUpdatingPassword,
                    onUpdateNewPassword = {
                        newPassword = it
                    },
                    onUpdateCurrentPassword = {
                        currentPassword = it
                    },
                    onUpdateConfirmPassword = {
                        confirmPassword = it
                    },
                    onUpdatePassword = {
                        onUpdatePassword(
                            currentPassword,
                            newPassword,
                            confirmPassword
                        )
                        updateVisibilityDialog(false)
                    },
                    onCancel = {
                        updateVisibilityDialog(false)
                    }
                )
            }
        )
    }

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
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onClickLogoff,
                modifier = Modifier,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.exit_to_app_24dp),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                   updateVisibilityDialog(true)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.change_password_icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.change_password),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        ChangePasswordCard(
            isVisibleDialog = false,
            isUpdatingPassword = false,
            updateVisibilityDialog = {},
            onUpdatePassword = { _, _, _ -> },
            onClickLogoff = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview1() {
    BillingTheme {
        ChangePasswordCard(
            isVisibleDialog = true,
            isUpdatingPassword = false,
            updateVisibilityDialog = {},
            onUpdatePassword = { _, _, _ -> },
            onClickLogoff = {}
        )
    }
}