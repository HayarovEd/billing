package com.impuls.billing.ui.payment_history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            contentColor = if (isChecked) 
                MaterialTheme.colorScheme.onPrimaryContainer 
            else 
                MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isChecked) 2.dp else 0.dp
        ),
        border = if (isChecked) 
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary) 
        else null,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(90.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringValue,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Start),
                imageVector = icon,
                contentDescription = null,
                tint = if (isChecked) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
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
            stringValue = "1000 ₽",
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
            stringValue = "-1000 ₽",
            onClick = {}
        )
    }
}
