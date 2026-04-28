package com.impuls.billing.ui.uikit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        shape = RoundedCornerShape(30.dp),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiTextField2(
    modifier: Modifier = Modifier,
    content: String,
    label: String,
    leadingText: String? = null,
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
        placeholder = {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
        leadingIcon = {
            leadingText?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        shape = RoundedCornerShape(30.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error,
        )
    )
}

@Composable
fun UiTextField3(
    modifier: Modifier = Modifier,
    content: String,
    label: String,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    borderColor: Color = colorScheme.onBackground,
    isOnlyDigit: Boolean = false,
    isOnlyLetter: Boolean = false,
    maxLines: Int = 1,
    textSize: TextUnit = 8.sp,
    onClickContent: (String) -> Unit,
    onClickTrailingIcon: () -> Unit = {},
) {
    val pattern = remember { Regex("^\\d*\\.?\\d*$") }
    BasicTextField(
        value = content,
        onValueChange = {text->
            if (isOnlyDigit) {
                if (text.isEmpty() || text.matches(pattern)) {
                    onClickContent(text)
                }
            } else if (isOnlyLetter) {
                if (text.all { it.isLetter() }) {
                    onClickContent(text)
                }
            } else {
                onClickContent(text)
            }
        },
        modifier = modifier
            .fillMaxWidth(),
        enabled = enabled,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        readOnly = readOnly,
       // textStyle = MaterialTheme.typography.bodyLarge.copy(color = colorScheme.onPrimaryContainer),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = borderColor
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp,),
                contentAlignment = Alignment.CenterStart
            ) {
                if (content.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .height(50.dp)
                            .align(Alignment.CenterStart),
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = textSize,
                            color = colorScheme.onBackground
                        ),
                    )
                }
                trailingIcon?.let {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clickable(
                                onClick = onClickTrailingIcon
                            ),
                        imageVector = it,
                        contentDescription = ""
                    )
                }
                innerTextField()
            }
        }
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
private fun UiTextFieldView10() {
    BillingTheme {
        UiTextField2(
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

@Preview(
    showBackground = true
)
@Composable
private fun UiTextFieldView5() {
    BillingTheme {
        UiTextField3(
            content = "world!",
            label = "hallo",
            onClickContent = {}
        )
    }
}