package com.impuls.billing.ui.uikit

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun UiIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    size: Dp = 24.dp,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.secondary,
    buttonColor: Color = Color.Transparent,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = buttonColor
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = icon,
            contentDescription = "",
            tint = color
        )
    }
}