package com.impuls.billing.ui.payment_history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun ContentHistoryBottomSheet(
    modifier: Modifier = Modifier,
    onClickPeriodToday: () -> Unit,
    onClickPeriodWeek: () -> Unit,
    onClickPeriodMonth: () -> Unit,
    onClickPeriodCustom: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        PeriodSegmentedButton(
            onClick = onClickPeriodToday,
            text = stringResource(R.string.today)
        )
        PeriodSegmentedButton(
            onClick = onClickPeriodWeek,
            text = stringResource(R.string.week)
        )
        PeriodSegmentedButton(
            onClick = onClickPeriodMonth,
            text = stringResource(R.string.month)
        )
        PeriodSegmentedButton(
            onClick = onClickPeriodCustom,
            text = stringResource(R.string.choose_period)
        )
    }
}


@Composable
private fun PeriodSegmentedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.keyboard_arrow_right_24dp),
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BillingTheme {
        ContentHistoryBottomSheet(
            onClickPeriodToday = {},
            onClickPeriodWeek = {},
            onClickPeriodMonth = {},
            onClickPeriodCustom = {}
        )
    }
}