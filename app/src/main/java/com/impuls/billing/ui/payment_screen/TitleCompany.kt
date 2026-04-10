package com.impuls.billing.ui.payment_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.toComposeColor
import kotlin.io.encoding.Base64

private const val BASE64_SUBSTRING = "base64,"

@Composable
fun TitleCompany(
    modifier: Modifier = Modifier,
    name: String,
    image: String,
    colorString: String
) {
    val color = colorString.toComposeColor()

    val base64ImageByteArray = Base64.decode(image.substringAfter(BASE64_SUBSTRING))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            color.copy(alpha = 0.15f),
                            Color.White
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Текст с названием компании
            Text(
                modifier = Modifier
                    .weight(2f),
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF1A1A1A)
            )

            // Иконка компании
            AsyncImage(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White),
                model = base64ImageByteArray,
                contentDescription = "Логотип компании",
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BillingTheme {
        TitleCompany(
            colorString = "FEAD16",
            image = "data:image/png;base64,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",
            name = "НПО ЦОДИТ, Телинт, Инновационный центр Верхнекамья, НПО Импульс"
        )
    }
}