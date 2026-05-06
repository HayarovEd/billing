package com.impuls.billing.ui.profile_screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.impuls.billing.R
import com.impuls.billing.domain.model.ClientVroup
import com.impuls.billing.domain.model.ModifierData
import com.impuls.billing.domain.model.TypeBlockVGroup
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.asTypeBlockVGroupText
import kotlin.random.Random

@Composable
fun TarifCardFL(
    modifier: Modifier = Modifier,
    clientVgroup: ClientVroup,
    onClickUnblock: () -> Unit,
    onClickPayment: () -> Unit,
) {
    val containerColorColor = when (clientVgroup.blocked) {
        TypeBlockVGroup.UNBLOCKED -> MaterialTheme.colorScheme.surface
        TypeBlockVGroup.USER_BLOCKED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.1f)
        TypeBlockVGroup.ADMIN_BLOCKED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        TypeBlockVGroup.OFF_ACTIVE -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        TypeBlockVGroup.BALANCE_BLOCKED -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
    }
    var widthCard by remember { mutableIntStateOf(0) }
    var heightCard by remember { mutableIntStateOf(0) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                widthCard = (size.width)
                heightCard = (size.height)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
         //   containerColor = containerColorColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box( modifier = Modifier
            .fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = containerColorColor)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = clientVgroup.tarifDescr,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(64.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        modifier = modifier.fillMaxWidth(),
                                        text = "${clientVgroup.speed}\n${stringResource(R.string.unit_m_s)}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = clientVgroup.agentDescr,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            if (clientVgroup.blocked == TypeBlockVGroup.USER_BLOCKED) {
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clickable(
                                            onClick = onClickUnblock
                                        )
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.unlocked_black),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding()
                                                .size(24.dp)
                                        )
                                    }
                                }
                            }
                            if (clientVgroup.blocked == TypeBlockVGroup.UNBLOCKED) {
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clickable(
                                            onClick = onClickUnblock
                                        )
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.lock_padlock),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding()
                                                .size(24.dp)
                                        )
                                    }
                                }
                            }
                            if (clientVgroup.blocked == TypeBlockVGroup.BALANCE_BLOCKED) {
                                Spacer(modifier = Modifier.weight(1f))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clickable(
                                            onClick = onClickPayment
                                        )
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.payment_card_24dp),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding()
                                                .size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.abonent_price, clientVgroup.serviceRent),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (clientVgroup.blocked!= TypeBlockVGroup.UNBLOCKED) {
                val color = MaterialTheme.colorScheme.errorContainer
                val textColor = MaterialTheme.colorScheme.onErrorContainer
                val textMeasurer = rememberTextMeasurer()
                val measuredText =
                    textMeasurer.measure(
                        text = AnnotatedString(clientVgroup.blocked.asTypeBlockVGroupText().asString()),
                        style = TextStyle(fontSize = 10.sp, color = textColor),
                        overflow = TextOverflow.Visible
                    )

                Canvas(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    val pathWidth = heightCard * 0.2f
                    val path = Path().apply {
                        moveTo(widthCard - pathWidth - heightCard * 0.4f, 0f)
                        lineTo(widthCard - heightCard * 0.4f, 0f)
                        lineTo(widthCard.toFloat(), heightCard * 0.40f)
                        lineTo(widthCard.toFloat(), heightCard * 0.4f + pathWidth)
                        lineTo(widthCard - pathWidth - heightCard * 0.4f, 0f)
                    }
                    drawPath(
                        path = path,
                        color = color
                    )
                    val xM = (widthCard - pathWidth*0.5f - heightCard * 0.4f + widthCard)/2 - measuredText.size.width/2
                    val yM = (0f + heightCard * 0.4f)/2
                    rotate(degrees = 45f, pivot =  Offset(xM+measuredText.size.width/2, yM)) {
                        drawText(
                            measuredText,
                            topLeft = Offset(xM, yM)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val vgroups = (1..1).map {
        ClientVroup(
            agrmId = 45,
            vgId = it.toLong(),
            blocked = TypeBlockVGroup.UNBLOCKED,
            tarifId = it * 10L,
            tarifDescr = "Просто - 100 (Импульс) 1000 руб",
            login = "login $it",
            serviceVolume = 1000L,
            serviceRent = 500.0,
            addresses = emptyList(),
            speed = 100,
            agentDescr = "Netflow ООО НПО ИМПУЛЬС",
            currentModifier = ModifierData(
                type = "ddds",
                value = Random.nextDouble(0.8, 1.0),
            )
        )
    }
    BillingTheme {
        TarifCardFL(
            clientVgroup = vgroups.first(),
            onClickUnblock = {},
            onClickPayment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview1() {
    val vgroups = (1..1).map {
        ClientVroup(
            agrmId = 45,
            vgId = it.toLong(),
            blocked = TypeBlockVGroup.USER_BLOCKED,
            tarifId = it * 10L,
            tarifDescr = "Просто - 100 (Импульс) 1000 руб",
            login = "login $it",
            serviceVolume = 1000L,
            serviceRent = 500.0,
            addresses = emptyList(),
            speed = 100,
            agentDescr = "Netflow ООО НПО ИМПУЛЬС",
            currentModifier = ModifierData(
                type = "ddds",
                value = Random.nextDouble(0.8, 1.0),
            )
        )
    }
    BillingTheme {
        TarifCardFL(
            clientVgroup = vgroups.first(),
            onClickUnblock = {},
            onClickPayment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    val vgroups = (1..1).map {
        ClientVroup(
            agrmId = 45,
            vgId = it.toLong(),
            blocked = TypeBlockVGroup.ADMIN_BLOCKED,
            tarifId = it * 10L,
            tarifDescr = "Просто - 100 (Импульс) 1000 руб",
            login = "login $it",
            serviceVolume = 1000L,
            serviceRent = 500.0,
            addresses = emptyList(),
            speed = 100,
            agentDescr = "Netflow ООО НПО ИМПУЛЬС",
            currentModifier = ModifierData(
                type = "ddds",
                value = Random.nextDouble(0.8, 1.0),
            )
        )
    }
    BillingTheme {
        TarifCardFL(
            clientVgroup = vgroups.first(),
            onClickUnblock = {},
            onClickPayment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview3() {
    val vgroups = (1..1).map {
        ClientVroup(
            agrmId = 45,
            vgId = it.toLong(),
            blocked = TypeBlockVGroup.OFF_ACTIVE,
            tarifId = it * 10L,
            tarifDescr = "Просто - 100 (Импульс) 1000 руб",
            login = "login $it",
            serviceVolume = 1000L,
            serviceRent = 500.0,
            addresses = emptyList(),
            speed = 100,
            agentDescr = "Netflow ООО НПО ИМПУЛЬС",
            currentModifier = ModifierData(
                type = "ddds",
                value = Random.nextDouble(0.8, 1.0),
            )
        )
    }
    BillingTheme {
        TarifCardFL(
            clientVgroup = vgroups.first(),
            onClickUnblock = {},
            onClickPayment = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview4() {
    val vgroups = (1..1).map {
        ClientVroup(
            agrmId = 45,
            vgId = it.toLong(),
            blocked = TypeBlockVGroup.BALANCE_BLOCKED,
            tarifId = it * 10L,
            tarifDescr = "Просто - 100 (Импульс) 1000 руб",
            login = "login $it",
            serviceVolume = 1000L,
            serviceRent = 500.0,
            addresses = emptyList(),
            speed = 100,
            agentDescr = "Netflow ООО НПО ИМПУЛЬС",
            currentModifier = ModifierData(
                type = "ddds",
                value = Random.nextDouble(0.8, 1.0),
            )
        )
    }
    BillingTheme {
        TarifCardFL(
            clientVgroup = vgroups.first(),
            onClickUnblock = {},
            onClickPayment = {},
        )
    }
}