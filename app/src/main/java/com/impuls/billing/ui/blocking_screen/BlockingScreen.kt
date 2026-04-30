package com.impuls.billing.ui.blocking_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.DateCalendar
import com.impuls.billing.ui.uikit.ObserveAsEvents
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun BlockingScreenRoot(
    viewModel: BlockingScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {

            else -> {}
        }
    }

    BlockingScreen(
        state = state,
        snakeBarHostState = snakeBarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun BlockingScreen(
    modifier: Modifier = Modifier,
    state: BlockingScreenState,
    snakeBarHostState: SnackbarHostState,
    onAction: (BlockingScreenAction) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snakeBarHostState) },
    ) { contentPadding ->
        if (state.isLoading) {
            Box(
                modifier = modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(15.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                Text(
                    text = state.selectedDate.toString(),
                    style = MaterialTheme.typography.labelMedium,
                )
                DateCalendar(
                    currentDate = state.selectedDate.toJavaLocalDate(),
                    updateCurrentDate = {
                        onAction(BlockingScreenAction.UpdateSelectedDate(it.toKotlinLocalDate()))
                    }
                )
            }
        }
    }

}

@Preview
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        BlockingScreen(
            state = BlockingScreenState(),
            snakeBarHostState = snackBarState,
            onAction = {}
        )
    }
}
