package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.TitleMethodContent
import com.impuls.billing.ui.uikit.TitleMethodContentCard

@Composable
fun ItemPaymentMethod(
    modifier: Modifier = Modifier,
    commission: Int,
    isChosenMethod: Boolean,
    titleContent: @Composable () -> Unit,
    updateSelect: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (isChosenMethod) MaterialTheme.colorScheme.onSecondaryContainer
            else MaterialTheme.colorScheme.primary
        ),
        onClick = updateSelect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            titleContent()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RadioButton(
                    selected = isChosenMethod,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = null // Click is handled by Card
                )
                Text(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(4.dp))
                        .background(
                            color = if (isChosenMethod) MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.primary
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    text = "${stringResource(R.string.commission)} $commission%",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isChosenMethod) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        ItemPaymentMethod(
            commission = 0,
            isChosenMethod = false,
            titleContent = {
                TitleMethodContent(
                    icons = listOf(
                        R.drawable.visaa,
                        R.drawable.master,
                        R.drawable.mir_logo,
                        R.drawable.pay_union_pay
                    ),
                    imageWidth = 128.dp
                )
            },
            updateSelect = {}
        )
    }
}

@Preview
@Composable
private fun Preview2() {
    BillingTheme {
        ItemPaymentMethod(
            commission = 0,
            isChosenMethod = true,
            titleContent = {
                TitleMethodContentCard(
                    name = stringResource(R.string.bank_card),
                    icons = listOf(
                        R.drawable.visaa,
                        R.drawable.master,
                        R.drawable.mir_logo,
                        R.drawable.pay_union_pay
                    )
                )
            },
            updateSelect = {}
        )
    }
}
