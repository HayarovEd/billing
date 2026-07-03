package com.impuls.chat.ui.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.res.stringResource
import com.impuls.chat.R
import com.impuls.chat.domain.utils.MASK
import com.impuls.chat.domain.utils.MASK_NUMBER
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.utils.Utils
import com.impuls.chat.ui.uikit.ObserveAsEvents
import com.impuls.chat.ui.uikit.PhoneVisualTransformation
import com.impuls.chat.ui.uikit.UiTextField
import com.impuls.chat.ui.uikit.asText
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreenRoot (
    viewModel: MainScreenViewModel = koinViewModel (),
    onNavigateToChat: (String, String, String, String, String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is MainScreenEvent.MessageEvent -> {
                snackBarState.showSnackbar(
                    event.messageEvent.asText().asString(context)
                )
            }
            is MainScreenEvent.NavigateEvent -> onNavigateToChat(event.chatId, state.name, state.login, state.password, state.email)
            is MainScreenEvent.OnMessage -> {
                snackBarState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )
            }
        }
    }

    MainScreen(
        state = state,
        onAction = viewModel::onAction,
        snackbarHostState = snackBarState
    )
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainScreenState,
    snackbarHostState: SnackbarHostState,
    onAction: (MainScreenAction) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_appeal),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                UiTextField(
                    content = state.name,
                    label = stringResource(R.string.your_name),
                    onClickContent = { onAction(MainScreenAction.UpdateName(it)) }
                )

                UiTextField(
                    content = state.email,
                    label = stringResource(R.string.email_label),
                    onClickContent = { onAction(MainScreenAction.UpdateEmail(it)) }
                )

                UiTextField(
                    content = state.phone,
                    label = stringResource(R.string.phone_label),
                    visualTransformation = PhoneVisualTransformation(MASK, MASK_NUMBER),
                    onClickContent = { onAction(MainScreenAction.UpdatePhone(it)) }
                )

                UiTextField(
                    content = state.message,
                    label = stringResource(R.string.message_label),
                    maxLines = 5,
                    onClickContent = { onAction(MainScreenAction.UpdateMessage(it)) }
                )

                Button(
                    onClick = { onAction(MainScreenAction.CreateChat) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(30.dp),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = stringResource(R.string.create_chat_button))
                    }
                }
            }
            
            if (state.isLoading) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }
    MainScreen(
        state = MainScreenState(
            name = "Test User",
            phone = "79026355319",
            email = "test@example.com",
            operId = 0
        ),
        snackbarHostState = snackBarState,
        onAction = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun MainScreenWithChatsPreview() {
    val snackBarState = remember { SnackbarHostState() }
    val mockGroup = Group(id = "1", name = "Техподдержка", operId = 1)
    val mockChats = listOf(
        Chat(
            connections = emptyList(),
            createdDate = Utils.convertToLocalDateTime("2026-06-23T08:16:07.132601Z"),
            email = "user@test.com",
            id = "chat_1",
            isOpen = true,
            isWork = true,
            name = "Проблема с доступом",
            phone = "79026355319",
            group = mockGroup,
            isActive = false,
            unreadMessagesCount = 1,
            login = "eree"
        ),
        Chat(
            connections = emptyList(),
            createdDate = Utils.convertToLocalDateTime("2026-06-23T08:16:07.132601Z"),
            email = "user@test.com",
            id = "chat_2",
            isOpen = true,
            isWork = true,
            name = "Вопрос по оплате",
            phone = "79026355319",
            group = mockGroup,
            isActive = true,
            unreadMessagesCount = 0,
            login = "eree"
        )
    )

    MainScreen(
        state = MainScreenState(
            name = "Test User",
            phone = "79026355319",
            email = "test@example.com",
            operId = 0,
            userChats = mockChats
        ),
        snackbarHostState = snackBarState,
        onAction = {}
    )
}
