package com.impuls.billing.ui.uikit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiTextField(
    modifier: Modifier = Modifier,
    content: String,
    label: String,
    error: String = "",
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isOnlyDigit: Boolean = false,
    maxLines: Int = 1,
    isError: Boolean = false,
    onClickContent: (String) -> Unit,
    onClickTrailingIcon: () -> Unit = {},
) {
    OutlinedTextField(
        value = content,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
                shape = MaterialTheme.shapes.medium
            ),
        onValueChange = { text ->
            if (isOnlyDigit) {
                if (text.all { it.isDigit() }) {
                    onClickContent(text)
                }
            } else {
                onClickContent(text)
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
        isError = isError,
        supportingText = {
            if (isError) {
                Text(
                    text = error,
                    style = Typography.labelSmall
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        trailingIcon = {
            if (trailingIcon != null) {
                UiIconButton(
                    icon = trailingIcon,
                    onClick = onClickTrailingIcon,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        label = {
            Text(
                text = label,
                style = Typography.labelSmall
            )
        },
        colors = colors()
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun UiTextFieldView() {
    BillingTheme {
        UiTextField(
            content = "",
            label = "hallo",
            onClickContent = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun UiTextFieldView2() {
    BillingTheme {
        UiTextField(
            content = "world!",
            label = "hallo",
            onClickContent = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UiTextFieldView3() {
    BillingTheme {
        UiTextField(
            content = "",
            label = "hallo",
            onClickContent = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UiTextFieldView4() {
    BillingTheme {
        UiTextField(
            content = "world!",
            label = "hallo",
            error = "error",
            isError = true,
            onClickContent = {}
        )
    }
}