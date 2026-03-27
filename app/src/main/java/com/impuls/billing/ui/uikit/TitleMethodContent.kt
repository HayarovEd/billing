package com.impuls.billing.ui.uikit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.impuls.billing.R
import com.impuls.billing.ui.theme.BillingTheme

@Composable
fun TitleMethodContentCard(
    modifier: Modifier = Modifier,
    name: String,
    icons: List<Int>
) {
    Column(modifier= modifier) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(icons) {
                Image(
                    painter = painterResource(it),
                    contentDescription = null
                )
            }
        }
    }
}


@Composable
fun TitleMethodContent(
    modifier: Modifier = Modifier,
    imageWidth: Dp,
    icons: List<Int>
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(icons) {
            Image(
                modifier = modifier.width(imageWidth),
                painter = painterResource(it),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        TitleMethodContentCard(
            name = stringResource(R.string.bank_card),
            icons = listOf(
                R.drawable.visaa,
                R.drawable.master,
                R.drawable.mir_logo,
                R.drawable.pay_union_pay
            )
        )
    }
}