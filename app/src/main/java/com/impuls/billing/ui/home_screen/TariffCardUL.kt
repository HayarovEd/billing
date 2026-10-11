package com.impuls.billing.ui.home_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.ModifierData
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.TypeBlockVGroup
import com.impuls.billing.ui.theme.BillingTheme


@Composable
fun TariffCardUL(
    data: ClientVroup,
    width: Dp,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor =  MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 0.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
        ),
        onClick = onClick
    ) {
        Column(
            modifier = modifier
                .width(width)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.login,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (data.blocked!= TypeBlockVGroup.UNBLOCKED) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = stringResource(R.string.is_blocked),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = stringResource(R.string.is_active),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${stringResource(R.string.tarif)}:",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    modifier = modifier.basicMarquee(),
                    text = data.tarifDescr,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(
                        R.string.abonent_price,
                        data.serviceRent * data.currentModifier.value
                    ),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TariffListScreenUL() {
    val sampleData = ClientVroup(
        agrmId = 8220,
        vgId = 9022,
        blocked = TypeBlockVGroup.UNBLOCKED,
        tarifId = 108,
        tarifDescr = "Для подключения Телинт (просто 50)",
        login = "tb02376k93",
        serviceRent = 1000.0,
        serviceVolume = 2000,
        addresses = emptyList(),
        agentDescr = "Netflow ООО НПО ИМПУЛЬС",
        speed = 100,
        currentModifier = ModifierData(
            type = "rate",
            value = 0.95
        )
    )
    BillingTheme() {
        TariffCardUL(
            data = sampleData,
            width = 600.dp,
            isSelected = true,
            onClick = {}
        )
    }
}