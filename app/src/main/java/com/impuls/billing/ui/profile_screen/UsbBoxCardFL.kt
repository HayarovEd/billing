package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.VgroupUsbBox
import com.impuls.billing.ui.theme.BillingTheme
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun UsbBoxCardFL(
    modifier: Modifier = Modifier,
    vGroupUsbBoxes: List<VgroupUsbBox>,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onSurface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.usb_boxes),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (vGroupUsbBoxes.isEmpty()) {
                Text(
                    text = stringResource(R.string.not_usb_boxes),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(vGroupUsbBoxes) { usbBox->
                        Column() {
                            Text(
                                text = usbBox.tarDescr,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.abonent_price, (usbBox.rate*usbBox.price)),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val usBoxes = (1..5).map {
        VgroupUsbBox(
            activated = Clock.System.now().minus(it, DateTimeUnit.DAY, TimeZone.UTC)
                .toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ),
            state = it.toLong(),
            rate = 1.0,
            price = it * 100.0,
            vgLogin = "login $it",
            tarDescr = "Для подключения Телинт (просто ${it * 10})",
            symbol = "руб."
        )
    }
    BillingTheme {
        UsbBoxCardFL(
            vGroupUsbBoxes = usBoxes
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview1() {
    BillingTheme {
        UsbBoxCardFL(
            vGroupUsbBoxes = emptyList()
        )
    }
}