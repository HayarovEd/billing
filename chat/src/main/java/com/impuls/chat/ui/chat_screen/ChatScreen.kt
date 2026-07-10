package com.impuls.chat.ui.chat_screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.bhuvaneshw.pdf.compose.PdfSource
import com.bhuvaneshw.pdf.compose.PdfViewer
import com.bhuvaneshw.pdf.compose.rememberPdfState
import com.bhuvaneshw.pdf.resource.DefaultNetworkResourceHandler
import com.impuls.chat.R
import com.impuls.chat.domain.model.BaseMessage
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Connection
import com.impuls.chat.domain.model.Group
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.MessageFile
import com.impuls.chat.domain.model.MimeTypes
import com.impuls.chat.domain.model.Role
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.User
import com.impuls.chat.domain.utils.BASE_CHAT_URL
import com.impuls.chat.domain.utils.Utils
import com.impuls.chat.ui.uikit.ObserveAsEvents
import com.impuls.chat.ui.uikit.UiIconButton
import kotlinx.datetime.TimeZone
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

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
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

    fun onAttachFile() {
        filePickerLauncher.launch("*/*")
    }

    fun onAttachImage() {
        imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    fun onAttachCamera() {
        val directory = context.externalCacheDir ?: context.cacheDir
        val file = File.createTempFile("camera_image_${System.currentTimeMillis()}", ".jpg", directory)
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
        onAction = viewModel::onAction,
        onAttachFile = ::onAttachFile,
        onAttachImage = ::onAttachImage,
        onAttachCamera = ::onAttachCamera,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    modifier: Modifier = Modifier,
    state: ChatScreenState,
    snackbarHostState: SnackbarHostState,
    onAction: (ChatScreenAction) -> Unit,
    onAttachFile: () -> Unit,
    onAttachImage: () -> Unit,
    onAttachCamera: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }
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
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            MessageList(
                messages = state.allMessages,
                token = state.token,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .fillMaxWidth(),
                onAction = onAction
            )

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
private fun MediaDialog(
    file: MessageFile,
    token: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val url = remember(file.fileUrl) { BASE_CHAT_URL + "files/" + file.fileUrl }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            when (val mime = file.mimeType) {
                is MimeTypes.Image -> {
                    val imageRequest = remember(url, token, mime) {
                        val headers = NetworkHeaders.Builder()
                            .set("Authorization", "Bearer $token")
                            .set("Content-Type", mime.mimeType)
                            .build()

                        ImageRequest.Builder(context)
                            .data(url)
                            .apply {
                                if (mime == MimeTypes.Image.SVG_XML) {
                                    decoderFactory(SvgDecoder.Factory())
                                }
                            }
                            .httpHeaders(headers)
                            .build()
                    }

                    AsyncImage(
                        model = imageRequest,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                is MimeTypes.Video -> {
                    VideoPlayer(
                        url = url,
                        token = token,
                        isUseController = true,
                        isPlayWhenReady = false,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                is MimeTypes.MimeType -> {
                    if (mime == MimeTypes.MimeType.PDF) {
                        val pdfState = rememberPdfState(
                            PdfSource.Url(url)
                        )
                        val networkResourceHandler = remember(token) {
                            DefaultNetworkResourceHandler(
                                beforeConnect = {
                                    setRequestProperty("Authorization", "Bearer $token")
                                }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .clickable(enabled = false) {}
                        ) {
                            PdfViewer(
                                pdfState = pdfState,
                                modifier = Modifier.fillMaxSize(),
                                onCreateViewer = {
                                    this.networkResourceHandler = networkResourceHandler
                                    this.onReady {
                                        this.ui.toolbarEnabled = true
                                        this.ui.viewerScrollbar = true
                                        this.zoomTo(com.bhuvaneshw.pdf.PdfViewer.Zoom.PAGE_FIT)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            UiIconButton(
                icon = ImageVector.vectorResource(R.drawable.close_24dp),
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                color = Color.White
            )
        }
    }
}

@Composable
private fun MessageList(
    messages: List<BaseMessage>,
    token: String,
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
        items(
            items = messages,
            key = { it.id }
        ) { message ->
            MessageItem(
                message = message,
                token = token,
                onAction = onAction
            )
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
        onAction = {},
        onAttachFile = {},
        onAttachImage = {},
        onAttachCamera = {},
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
    )
}

