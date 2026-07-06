package com.impuls.billing.ui.blocking_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.BlockVgroup
import com.impuls.billing.domain.model.TypeBlockVGroup
import com.impuls.billing.domain.utils.MAX_YEAR
import com.impuls.billing.domain.utils.parseToLocalDateTime
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.asTypeBlockVGroupText
import com.impuls.billing.ui.uikit.toDebitUi2
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun ItemHistoryBlock(
    modifier: Modifier = Modifier,
    historyBlock: BlockVgroup,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть: иконка и дата
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Иконка дебета
                Icon(
                    imageVector = if (historyBlock.timeTo.year == MAX_YEAR) ImageVector.vectorResource(
                        R.drawable.lock_padlock
                    ) else ImageVector.vectorResource(R.drawable.unlocked_black),
                    contentDescription = "Lock",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                Column {
                    Text(
                        text = stringResource(R.string.date_set_block),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = historyBlock.changeTime?.date?.toDebitUi2(context)?:"----",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Text(
                        text = stringResource(R.string.date_unblock),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = if (historyBlock.timeTo.year == MAX_YEAR) "----" else historyBlock.timeTo.date.toDebitUi2(context),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            Text(
                text = historyBlock.blkReq.asTypeBlockVGroupText().asString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    BillingTheme {
        ItemHistoryBlock(
            historyBlock = BlockVgroup(
                changeTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                agrmId = 28627,
                agrmNum = "ИТ004368Ф",
                blkReq = TypeBlockVGroup.USER_BLOCKED,
                comment = "",
                freewill = false,
                groupId = 0,
                id = 0,
                login = "tb6b97xdi8",
                mgrDescr = "",
                mgrName = "Зубова Надежда Васильевна",
                recordId = 11111,
                timeTo = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                uid = 28423,
                unBlockedManagerDescr = "",
                unBlockedManagerLogin = "",
                unBlockedManagerName = "",
                username = "",
                vgId = 34,
            )
        )
    }
}

@Preview
@Composable
private fun Preview1() {
    BillingTheme {
        ItemHistoryBlock(
            historyBlock = BlockVgroup(
                changeTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                agrmId = 28627,
                agrmNum = "ИТ004368Ф",
                blkReq = TypeBlockVGroup.USER_BLOCKED,
                comment = "",
                freewill = false,
                groupId = 0,
                id = 0,
                login = "tb6b97xdi8",
                mgrDescr = "",
                mgrName = "Зубова Надежда Васильевна",
                recordId = 11111,
                timeTo = parseToLocalDateTime("9999-12-31 23:59:59"),
                uid = 28423,
                unBlockedManagerDescr = "",
                unBlockedManagerLogin = "",
                unBlockedManagerName = "",
                username = "",
                vgId = 34,
            )
        )
    }
}