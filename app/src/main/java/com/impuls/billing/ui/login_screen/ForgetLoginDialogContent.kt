package com.impuls.billing.ui.login_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun ForgetLoginDialogContent(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.help_phone),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(5.dp))
            Text(
                modifier = modifier
                    .fillMaxWidth(),
                text = stringResource(R.string.need_passport_data),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(5.dp))
            OutlinedButton(
                onClick = onCloseClick,
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = stringResource(R.string.ok_),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun Preview() {
    BillingTheme {
        ForgetLoginDialogContent(
            onCloseClick = {}
        )
    }
}