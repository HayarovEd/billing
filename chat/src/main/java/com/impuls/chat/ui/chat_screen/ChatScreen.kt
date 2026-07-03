package com.impuls.chat.ui.chat_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.chat.R
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Connection
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.Role
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.model.User
import com.impuls.chat.domain.utils.Utils
import com.impuls.chat.ui.uikit.ObserveAsEvents
import com.impuls.chat.ui.uikit.UiIconButton
import com.impuls.chat.ui.uikit.UiTextField
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Clock

@Composable
fun ChatScreenRoot(
    viewModel: ChatScreenViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarState = remember { SnackbarHostState() }

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
        }
    }

    ChatScreen(
        state = state,
        snackbarHostState = snackBarState,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    snackbarHostState: SnackbarHostState,
    onAction: (ChatScreenAction) -> Unit,
) {
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
                message = state.currentMessage,
                onMessageChange = { onAction(ChatScreenAction.UpdateCurrentMessage(it)) },
                onSend = { onAction(ChatScreenAction.SendMessage(it)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            MessageList(
                messages = state.allMessages,
                modifier = Modifier.fillMaxSize(),
                onAction = onAction
            )

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun MessageList(
    messages: List<Message>,
    modifier: Modifier = Modifier,
    onAction: (ChatScreenAction) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = false
    ) {
        items(messages.sortedBy { it.date }) { message ->
            MessageItem(message = message, onAction = onAction)
        }
    }
}

@Composable
private fun MessageItem(
    message: Message,
    onAction: (ChatScreenAction) -> Unit
) {
    LaunchedEffect(message.id, message.statusMessage) {
        if (message.isSupport && message.statusMessage != StatusMessage.READED) {
            onAction(ChatScreenAction.SetMessageAsRead(message.id))
        }
    }
    val alignment = if (message.isSupport) Alignment.Start else Alignment.End
    val backgroundColor = if (message.isSupport) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val textColor = if (message.isSupport) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isSupport) 0.dp else 16.dp,
                        bottomEnd = if (message.isSupport) 16.dp else 0.dp
                    )
                )
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Column {
                Text(text = message.text, color = textColor)
                if (!message.isSupport) {
                    StatusIndicator(status = message.statusMessage)
                }
            }
        }
        Text(
            text = message.date.toString().substring(11, 16),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun StatusIndicator(status: StatusMessage) {
    val text = when (status) {
        StatusMessage.SENDED -> "..."
        StatusMessage.DELIVERED -> "✓"
        StatusMessage.ERROR_SEND -> "!"
        StatusMessage.READED -> "✓✓"
    }
    Text(
        text = text,
        fontSize = 10.sp,
        modifier = Modifier.padding(top = 2.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun ChatInput(
    message: String,
    onMessageChange: (String) -> Unit,
    onSend: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UiTextField(
            content = message,
            label = stringResource(R.string.message_label),
            modifier = Modifier.weight(1f),
            onClickContent = onMessageChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        UiIconButton(
            icon = ImageVector.vectorResource(R.drawable.send_24dp),
            onClick = { if (message.isNotBlank()) onSend(message) },
            enabled = message.isNotBlank()
        )
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
        onAction = {}
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
            statusMessage = StatusMessage.DELIVERED
        ),
        Message(
            date = now,
            id = "2",
            isSupport = true,
            text = "Добрый день! Опишите, пожалуйста, подробнее.",
            statusMessage = StatusMessage.DELIVERED
        ),
        Message(
            date = now,
            id = "3",
            isSupport = false,
            text = "Не загружается список чатов на главном экране",
            statusMessage = StatusMessage.DELIVERED
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
        onAction = {}
    )
}

