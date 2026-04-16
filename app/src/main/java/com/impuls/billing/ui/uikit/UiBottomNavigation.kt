package com.impuls.billing.ui.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun UiBottomNavigation(
    modifier: Modifier = Modifier,
    onClickOpenInfo: () -> Unit,
    onClickToPayment: () -> Unit,
    onClickOpenHelp: () -> Unit,
) {

    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(start = 15.dp, end = 15.dp)
            .clip(
                shape = RoundedCornerShape(15.dp)
            )
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        UiIconButton(
            modifier = modifier,
            icon = ImageVector.vectorResource(R.drawable.info_24dp),
            buttonColor = MaterialTheme.colorScheme.onPrimary,
            onClick = onClickOpenInfo
        )
        Spacer(modifier = modifier.width(10.dp))
        Button(
            onClick = onClickToPayment,
            modifier = Modifier
                .weight(1f)
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
                    imageVector = ImageVector.vectorResource(R.drawable.payment_card_24dp),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = stringResource(R.string.add_balance),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = modifier.width(10.dp))
        UiIconButton(
            icon = ImageVector.vectorResource(R.drawable.chat_24dp),
            buttonColor =  MaterialTheme.colorScheme.onPrimary,
            onClick = onClickOpenHelp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UiBottomNavigation1() {
    BillingTheme() {
        UiBottomNavigation(
            onClickOpenHelp = {},
            onClickOpenInfo = {},
            onClickToPayment = {}
        )
    }

}