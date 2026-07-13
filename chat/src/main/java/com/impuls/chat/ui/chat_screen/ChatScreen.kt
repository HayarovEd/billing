package com.impuls.chat.ui.chat_screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.chat.R
import com.impuls.chat.domain.model.BaseMessage
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Connection
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.Role
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.User
import com.impuls.chat.domain.utils.Utils
import com.impuls.chat.ui.uikit.ObserveAsEvents
import com.impuls.chat.ui.uikit.UiIconButton
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import java.io.File
import kotlin.time.Clock

@Composable
fun ChatScreenRoot(
    viewModel: ChatScreenViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarState = remember { SnackbarHostState() }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onAction(ChatScreenAction.AttachFile(it)) }
    }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            uri?.let { viewModel.onAction(ChatScreenAction.AttachFile(it)) }
        }

    val cameraImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let { viewModel.onAction(ChatScreenAction.AttachFile(it)) }
        }
    }

    var isShowScrollArrow by remember { mutableStateOf(false) }

    fun onAttachFile() {
        filePickerLauncher.launch("*/*")
    }

    fun onAttachImage() {
        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    fun onAttachCamera() {
        val directory = context.externalCacheDir ?: context.cacheDir
        val file =
            File.createTempFile("camera_image_${System.currentTimeMillis()}", ".jpg", directory)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        cameraImageUri.value = uri
        cameraLauncher.launch(uri)
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ChatScreenEvent.NavigateToLogin -> onNavigateToLogin()
            is ChatScreenEvent.OnMessage -> {
                snackBarState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )
            }

            is ChatScreenEvent.OpenAnotherApp -> {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(event.uri, event.mimeType.mimeType)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Открыть файл"))
            }
        }
    }

    BackHandler {
        onNavigateToLogin()
    }

    ChatScreen(
        state = state,
        snackbarHostState = snackBarState,
        isShowScrollArrow = isShowScrollArrow,
        onAction = viewModel::onAction,
        onAttachFile = ::onAttachFile,
        onAttachImage = ::onAttachImage,
        onAttachCamera = ::onAttachCamera,
        updateShowScrollArrow = {
            isShowScrollArrow = it
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    isShowScrollArrow: Boolean,
    snackbarHostState: SnackbarHostState,
    onAction: (ChatScreenAction) -> Unit,
    onAttachFile: () -> Unit,
    onAttachImage: () -> Unit,
    onAttachCamera: () -> Unit,
    updateShowScrollArrow: (Boolean) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val allMessagesFlat = remember(state.allMessages) { state.allMessages.values.flatten() }
    val firstUnreadSupportMsg = remember(allMessagesFlat) {
        allMessagesFlat.find { it.isSupport && it.statusMessage != StatusMessage.READED }
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(listState.canScrollForward) {
        updateShowScrollArrow(listState.canScrollForward)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = state.chat?.group?.name ?: "") },
                actions = {
                    UiIconButton(
                        icon = ImageVector.vectorResource(R.drawable.close_24dp),
                        onClick = { onAction(ChatScreenAction.CloseChat) }
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            ChatInput(
                modifier = Modifier.navigationBarsPadding(),
                message = state.currentMessage,
                attachedFiles = state.attachedFiles,
                menuExpanded = menuExpanded,
                onMessageChange = { onAction(ChatScreenAction.UpdateCurrentMessage(it)) },
                onSend = { onAction(ChatScreenAction.SendMessage(it)) },
                onAttachFile = onAttachFile,
                onAttachImage = onAttachImage,
                onAttachCamera = onAttachCamera,
                onRemoveFile = { onAction(ChatScreenAction.RemoveFile(it)) },
                updateMenuExpanded = {
                    menuExpanded = it
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            MessageList(
                messages = state.allMessages,
                token = state.token,
                listState = listState,
                allMessagesFlat = allMessagesFlat,
                firstUnreadSupportMsg = firstUnreadSupportMsg,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(),
                onAction = onAction
            )
            AnimatedVisibility(
                modifier = Modifier
                    .align(alignment = Alignment.CenterEnd),
                enter = fadeIn(),
                exit = fadeOut(),
                visible = isShowScrollArrow
            ) {
                UiIconButton(
                    modifier = Modifier
                        .size(48.dp),
                    buttonColor = MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                    icon = ImageVector.vectorResource(R.drawable.arrow_circle_down_48dp),
                    size = 48.dp,
                    onClick = {
                        scope.launch {
                            listState.scrollToItem(state.allMessages.size+allMessagesFlat.size)
                        }
                    }
                )
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

    if (state.selectedFile != null) {
        MediaDialog(
            file = state.selectedFile,
            token = state.token,
            onDismiss = { onAction(ChatScreenAction.ViewMedia(null)) }
        )
    }
}

@Composable
private fun MessageList(
    messages: Map<LocalDate, List<BaseMessage>>,
    token: String,
    allMessagesFlat: List<BaseMessage>,
    firstUnreadSupportMsg: BaseMessage?,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onAction: (ChatScreenAction) -> Unit
) {

    val monthRes = stringArrayResource(R.array.month)

    // Ищем первое непрочитанное сообщение от поддержки


    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            if (firstUnreadSupportMsg != null) {
                var itemPosition = 0
                run loop@{
                    messages.forEach { (_, msgsInDate) ->
                        itemPosition++ // заголовок даты
                        msgsInDate.forEach { msg ->
                            if (msg.id == firstUnreadSupportMsg.id) {
                                // Если это то самое сообщение, скроллим к разделителю (позиция itemPosition)
                                // или к самому сообщению (itemPosition + 1)
                                return@loop
                            }
                            itemPosition++
                        }
                    }
                }
                listState.scrollToItem(itemPosition)
            } else {
                val totalItemsCount = messages.size + allMessagesFlat.size
                if (totalItemsCount > 0) {
                    listState.scrollToItem(totalItemsCount - 1)
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = false
    ) {
        messages.forEach { (date, messagesByTime) ->
            stickyHeader {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(shape = RoundedCornerShape(20.dp))
                            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.2f))
                            .padding(3.dp),
                        text = "${date.day} ${monthRes[date.month.number - 1]}",
                        fontSize = 20.sp,
                    )
                }
            }
            items(
                items = messagesByTime,
                key = { it.id }
            ) { message ->
                if (message.id == firstUnreadSupportMsg?.id) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.unread_messages),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primaryContainer.copy(0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
                MessageItem(
                    message = message,
                    token = token,
                    onAction = onAction
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }
    ChatScreen(
        state = ChatScreenState(
            currentMessage = "Hello!",
            chat = null
        ),
        snackbarHostState = snackBarState,
        isShowScrollArrow = true,
        onAction = {},
        onAttachFile = {},
        onAttachImage = {},
        onAttachCamera = {},
        updateShowScrollArrow = {}
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ChatWithMessagesPreview() {
    val snackBarState = remember { SnackbarHostState() }
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    val mockGroup = Group(id = "1", name = "Техподдержка", operId = 1)
    val mockRole = Role(id = "1", name = "User")
    val mockUser = User(
        email = "user@test.com",
        id = "user_1",
        isDisabled = false,
        name = "Иван Иванов",
        role = mockRole
    )

    val mockMessages = listOf(
        Message(
            date = now,
            id = "1",
            isSupport = false,
            text = "Здравствуйте, у меня проблема с приложением",
            statusMessage = StatusMessage.DELIVERED,
            files = emptyList()
        ),
        Message(
            date = now,
            id = "2",
            isSupport = true,
            text = "Добрый день! Опишите, пожалуйста, подробнее.",
            statusMessage = StatusMessage.DELIVERED,
            files = emptyList()
        ),
        Message(
            date = now,
            id = "3",
            isSupport = false,
            text = "Не загружается список чатов на главном экране",
            statusMessage = StatusMessage.DELIVERED,
            files = emptyList()
        )
    )

    val mockChat = Chat(
        connections = listOf(
            Connection(
                end = null,
                id = "conn_1",
                messages = mockMessages,
                start = now,
                user = mockUser
            )
        ),
        createdDate = Utils.convertToLocalDateTime("2026-06-23T08:16:07.132601Z"),
        email = "user@test.com",
        id = "chat_1",
        isOpen = true,
        isWork = true,
        name = "Обращение №123",
        phone = "123456789",
        group = mockGroup,
        isActive = true,
        login = "user",
        unreadMessagesCount = 0
    )

    ChatScreen(
        state = ChatScreenState(
            chat = mockChat,
            currentMessage = "Печатаю ответ..."
        ),
        snackbarHostState = snackBarState,
        onAction = {},
        onAttachFile = {},
        onAttachImage = {},
        onAttachCamera = {},
        updateShowScrollArrow = {},
        isShowScrollArrow = false
    )
}

