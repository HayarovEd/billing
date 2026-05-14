package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.SharedPost
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.toDebitUi2
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@Composable
fun SharedPostsCard(
    modifier: Modifier = Modifier,
    sharedPosts: List<SharedPost>,
) {
    val context = LocalContext.current
    val windowSize = LocalWindowInfo.current.containerDpSize
    val windowWidth = windowSize.width

    var maxHeight by remember { mutableStateOf(0.dp) }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
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
                text = stringResource(R.string.news),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (sharedPosts.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_new),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            } else {
                LazyRow (
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(sharedPosts) { sp->
                        Card(
                            modifier = Modifier
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState())
                                .width(windowWidth*0.7f),
                            shape = RoundedCornerShape(5.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = sp.postTime.date.toDebitUi2(context),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                Text(
                                    text = sp.subject,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = sp.text,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
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
    val sharedPosts = (1..5).map {
        SharedPost(
            groupId = 1,
            pid = it,
            postCatId = 2,
            postTime = Clock.System.now().minus(it, DateTimeUnit.DAY, TimeZone.UTC)
                .toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ),
            status = 1,
            subject = "Title $it",
            text = "text shared post $it",
            uid = 0
        )
    }
    BillingTheme {
        SharedPostsCard(
            sharedPosts = sharedPosts
        )
    }
}
