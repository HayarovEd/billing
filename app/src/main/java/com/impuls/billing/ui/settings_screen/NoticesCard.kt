package com.impuls.billing.ui.settings_screen

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.ClientInfo
import com.impuls.billing.domain.model.ClientNotices
import com.impuls.billing.domain.model.Notice
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.RowNotice
import com.impuls.billing.ui.uikit.RowNoticeOnlyEmail
import com.impuls.billing.ui.uikit.RowNoticeWithVolume

@Composable
fun NoticesCard(
    modifier: Modifier = Modifier,
    clientInfo: ClientInfo,
    clientNotices: ClientNotices,
    updateCheckSms: (Boolean, Int) -> Unit,
    updateCheckEmail: (Boolean, Int) -> Unit,
    updateVolume: (String, Int) -> Unit,
    onClickSave: () -> Unit,
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
            // Заголовки столбцов
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.notice),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.amount),
                    modifier = Modifier.width(68.dp), // VolumeFieldWidth (60) + VolumeFieldPadding (8)
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.sms),
                    modifier = Modifier.width(48.dp), // CheckboxColumnWidth
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.email),
                    modifier = Modifier.width(48.dp), // CheckboxColumnWidth
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            TypeNotice.entries.forEachIndexed { index, type ->
                val notice = clientNotices.notices.firstOrNull { it.type == type.typeNumber }
                when (type) {
                    TypeNotice.WARNING_BALANCE -> {
                        RowNoticeWithVolume(
                            volume = notice?.value ?: "0",
                            typeNotice = type,
                            isCheckedEmail = notice?.email ?: false,
                            isCheckedSms = notice?.sms ?: false,
                            enableEmail = clientInfo.emailIsConfirmed,
                            enableSms = clientInfo.mobileIsConfirmed,
                            updateCheckSms = {
                                updateCheckSms(it, type.typeNumber)
                            },
                            updateCheckEmail = {
                                updateCheckEmail(it, type.typeNumber)
                            },
                            updateVolume = {
                                updateVolume(it, type.typeNumber)
                            }
                        )
                    }

                    TypeNotice.MONTHLY_REPORT -> {
                        RowNoticeOnlyEmail(
                            typeNotice = type,
                            isCheckedEmail = notice?.email ?: false,
                            enableEmail = clientInfo.emailIsConfirmed,
                            updateCheckEmail = {
                                updateCheckEmail(it, type.typeNumber)
                            }
                        )
                    }

                    else -> {
                        RowNotice(
                            typeNotice = type,
                            isCheckedEmail = notice?.email ?: false,
                            isCheckedSms = notice?.sms ?: false,
                            enableEmail = clientInfo.emailIsConfirmed,
                            enableSms = clientInfo.mobileIsConfirmed,
                            updateCheckSms = {
                                updateCheckSms(it, type.typeNumber)
                            },
                            updateCheckEmail = {
                                updateCheckEmail(it, type.typeNumber)
                            },
                        )
                    }
                }

                if (index < TypeNotice.entries.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = onClickSave,
                enabled = clientInfo.emailIsConfirmed || clientInfo.mobileIsConfirmed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.save_file),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.save),
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
    val notices = (1..3).map {
        Notice(
            descr = "descr",
            email = it % 2 == 0,
            messenger = false,
            name = "$it name",
            sms = it % 2 == 1,
            type = it,
            value = if (it == 1) "18" else "0"
        )
    }
    BillingTheme {
        NoticesCard(
            clientInfo = ClientInfo(
                email = "ed@rgh.ru",
                emailIsConfirmed = false,
                kontPerson = "User",
                mobile = "788888",
                mobileIsConfirmed = true,
                offerIsAccepted = true,
                passIsTemporary = false,
                password = "psde",
                phone = "",
                triggerContactDataReminder = false
            ),
            clientNotices = ClientNotices(
                uid = 8,
                notices = notices
            ),
            updateVolume = { _, _ -> },
            updateCheckEmail = { _, _ -> },
            updateCheckSms = { _, _ -> },
            onClickSave = {}
        )
    }
}