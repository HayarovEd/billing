package com.impuls.chat.ui.chat_screen

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.impuls.chat.R
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.utils.getFileName
import com.impuls.chat.domain.utils.getMimeTypeExtended
import com.impuls.chat.ui.uikit.UiIconButton
import com.impuls.chat.ui.uikit.UiTextField
import com.impuls.chat.ui.uikit.toImage

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    message: String,
    attachedFiles: List<Uri>,
    menuExpanded: Boolean,
    onMessageChange: (String) -> Unit,
    onSend: (String) -> Unit,
    onAttachFile: () -> Unit,
    onAttachImage: () -> Unit,
    onAttachCamera: () -> Unit,
    onRemoveFile: (Uri) -> Unit,
    updateMenuExpanded: (Boolean) -> Unit,
) {
    val context = LocalContext.current


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
    ) {
        if (attachedFiles.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(attachedFiles) { uri ->
                    AttachedFileItem(
                        uri = uri,
                        onRemove = { onRemoveFile(uri) },
                        mimeType = getMimeTypeExtended(context, uri)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                UiIconButton(
                    icon = Icons.Default.AttachFile,
                    onClick = { updateMenuExpanded(true) }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { updateMenuExpanded (false) }
                ) {
                    DropdownMenuItem(
                        text = { Text("Камера") },
                        onClick = {
                            updateMenuExpanded (false)
                            onAttachCamera()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Изображение") },
                        onClick = {
                            updateMenuExpanded (false)
                            onAttachImage()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Image, contentDescription = null)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Файл") },
                        onClick = {
                            updateMenuExpanded (false)
                            onAttachFile()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.AttachFile, contentDescription = null)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(4.dp))
            UiTextField(
                content = message,
                label = stringResource(R.string.message_label),
                maxLines = 5,
                modifier = Modifier.weight(1f),
                onClickContent = onMessageChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            UiIconButton(
                icon = ImageVector.vectorResource(R.drawable.send_24dp),
                onClick = { if (message.isNotBlank() || attachedFiles.isNotEmpty()) onSend(message) },
                enabled = message.isNotBlank() || attachedFiles.isNotEmpty(),
                color = if (message.isNotBlank() || attachedFiles.isNotEmpty()) Color(0xFF24A1DE) else Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatInputEmptyPreview() {
    MaterialTheme {
        ChatInput(
            modifier = Modifier.height(400.dp),
            message = "",
            attachedFiles = emptyList(),
            menuExpanded = true,
            onMessageChange = {},
            onSend = {},
            onAttachFile = {},
            onAttachImage = {},
            onAttachCamera = {},
            onRemoveFile = {},
            updateMenuExpanded = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatInputWithFilesPreview() {
    MaterialTheme {
        ChatInput(
            message = "Прикрепленные файлы",
            attachedFiles = listOf(Uri.EMPTY, Uri.EMPTY),
            menuExpanded = false,
            onMessageChange = {},
            onSend = {},
            onAttachFile = {},
            onAttachImage = {},
            onAttachCamera = {},
            onRemoveFile = {},
            updateMenuExpanded = {}
        )
    }
}

@Composable
private fun AttachedFileItem(
    uri: Uri,
    mimeType: String,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val mime = MimeTypes.convertToMimeType(mimeType)
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        when (mime) {
            is MimeTypes.Image -> {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            is MimeTypes.MimeType -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Image(
                        painter = painterResource(id = mime.toImage()),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    Text(
                        text = getFileName(context, uri),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color.Gray
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(20.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}