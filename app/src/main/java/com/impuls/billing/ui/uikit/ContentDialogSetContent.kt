package com.impuls.billing.ui.uikit

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

@Composable
fun ContentDialogSetContent(
    modifier: Modifier = Modifier,
    description: String,
    title: String,
    content: String,
    code: String,
    count: Int,
    isShowRepeatSendCode: Boolean,
    onUpdateContent: (String) -> Unit,
    onUpdateCode: (String) -> Unit,
    onConfirmContent: () -> Unit,
    onConfirmCode: () -> Unit,
    onRecentCode: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Иконка и заголовок
            if (title.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.info_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Описание
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Поле ввода
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Кнопка Отмена
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
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
                        imageVector = ImageVector.vectorResource(R.drawable.close_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                // Кнопка Подтвердить/Сохранить
                Button(
                    onClick = if (isShowRepeatSendCode) onConfirmCode else onConfirmContent,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Icon(
                        imageVector = if (isShowRepeatSendCode) ImageVector.vectorResource(R.drawable.check_circle_24dp) else ImageVector.vectorResource(
                            R.drawable.save_24dp
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isShowRepeatSendCode) stringResource(R.string.confirm)
                        else stringResource(R.string.save),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            // Кнопка повторной отправки
            if (isShowRepeatSendCode) {
                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onRecentCode,
                    enabled = count == 0,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (count == 0) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.autorenew_48dp),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.send_repeate_code),
                            style = MaterialTheme.typography.labelMedium
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${stringResource(R.string.send_repeate_code)} ($count)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
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
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "${title}:",
            style = MaterialTheme.typography.labelMedium
        )
        Spacer(modifier = modifier.width(3.dp))
        UiTextField(
            modifier = Modifier.weight(1.5f),
            content = content,
            label = "",
            onClickContent = { onUpdateContent(it) }
        )
    }
}

@Composable
fun ContentExitDialog(
    modifier: Modifier = Modifier,
    title: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Иконка предупреждения
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.warning_24dp),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Заголовок
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Кнопка Отмена
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
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
                        imageVector = ImageVector.vectorResource(R.drawable.close_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                // Кнопка Выход/Подтверждение
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.exit_to_app_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.exit),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
fun ContentDialogUpdatePassword(
    modifier: Modifier = Modifier,
    isUpdatingPassword: Boolean,
    currentPassword: String,
    newPassword: String,
    confirmPassword: String,
    onUpdateCurrentPassword: (String) -> Unit,
    onUpdateNewPassword: (String) -> Unit,
    onUpdateConfirmPassword: (String) -> Unit,
    onUpdatePassword: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                RowWithTextField(
                    title = stringResource(R.string.current_password),
                    content = currentPassword,
                    onUpdateContent = { onUpdateCurrentPassword(it) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                RowWithTextField(
                    title = stringResource(R.string.new_password),
                    content = newPassword,
                    onUpdateContent = { onUpdateNewPassword(it) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                RowWithTextField(
                    title = stringResource(R.string.confirm_password),
                    content = confirmPassword,
                    onUpdateContent = { onUpdateConfirmPassword(it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопки действий
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Кнопка Отмена
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
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
                        imageVector = ImageVector.vectorResource(R.drawable.close_24dp),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                // Кнопка Подтвердить/Сохранить
                Button(
                    onClick = onUpdatePassword,
                    modifier = Modifier.weight(1f),
                    enabled = !isUpdatingPassword
                            && currentPassword.isNotBlank()
                            && newPassword.isNotBlank()
                            && confirmPassword.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    if (isUpdatingPassword) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.reload_121122),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.check_circle_24dp),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.replace),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        ContentDialogSetContent(
            description = "Если у Вас изменился номер, пожалуйста, укажите его в соответствующем поле.",
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
            onRecentCode = {},
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    BillingTheme {
        ContentDialogSetContent(
            description = "Если у Вас изменился номер, пожалуйста, укажите его в соответствующем поле.",
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
            onRecentCode = {},
        )
    }
}

@Preview
@Composable
private fun Preview3() {
    BillingTheme {
        ContentDialogSetContent(
            description = "Если у Вас изменился номер, пожалуйста, укажите его в соответствующем поле.",
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
            onRecentCode = {},
        )
    }
}


@Preview
@Composable
private fun Preview4() {
    BillingTheme {
        ContentExitDialog(
            title = "Номер телефона",
            onCancel = {},
            onConfirm = {}
        )
    }
}

@Preview
@Composable
private fun Preview5() {
    BillingTheme {
        ContentDialogUpdatePassword(
            isUpdatingPassword = false,
            currentPassword = "1234",
            newPassword = "234",
            confirmPassword = "",
            onCancel = {},
            onUpdatePassword = {},
            onUpdateNewPassword = {},
            onUpdateConfirmPassword = {},
            onUpdateCurrentPassword = {}
        )
    }
}

@Preview
@Composable
private fun Preview6() {
    BillingTheme {
        ContentDialogUpdatePassword(
            isUpdatingPassword = true,
            currentPassword = "1234",
            newPassword = "234",
            confirmPassword = "",
            onCancel = {},
            onUpdatePassword = {},
            onUpdateNewPassword = {},
            onUpdateConfirmPassword = {},
            onUpdateCurrentPassword = {}
        )
    }
}