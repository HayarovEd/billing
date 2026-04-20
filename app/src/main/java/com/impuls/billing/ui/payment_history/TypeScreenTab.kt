package com.impuls.billing.ui.payment_history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun TypeScreenTab(
    modifier: Modifier = Modifier,
    stringValue: String,
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onClick: () -> Unit,
) {
    val windowSize = LocalWindowInfo.current.containerDpSize
    val windowHeight = windowSize.height
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
            contentColor = if (isChecked) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isChecked) 0.dp else 4.dp
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(windowHeight/5)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringValue,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Icon(
                modifier = Modifier
                    .align(alignment = Alignment.BottomStart)
                    .size(windowHeight/10),
                imageVector = icon,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        TypeScreenTab(
            icon = ImageVector.vectorResource(R.drawable.add_card_24dp),
            title = stringResource(R.string.deposits),
            stringValue = "1000",
            isChecked = true,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun Preview1() {
    BillingTheme {
        TypeScreenTab(
            icon = ImageVector.vectorResource(R.drawable.payment_arrow_down_24dp),
            title = stringResource(R.string.debits),
            isChecked = false,
            stringValue = "-1000",
            onClick = {}
        )
    }
}