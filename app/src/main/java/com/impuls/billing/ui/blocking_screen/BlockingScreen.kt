package com.impuls.billing.ui.blocking_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.domain.model.PeriodHistory
import com.impuls.billing.domain.model.TypeBottomSheet
import com.impuls.billing.ui.payment_history.ContentCalendarBottomSheet
import com.impuls.billing.ui.payment_history.ContentHistoryBottomSheet
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.NothingFind
import com.impuls.billing.ui.uikit.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun BlockingScreenRoot(
    viewModel: BlockingScreenViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var typeBottomSheet by remember { mutableStateOf(TypeBottomSheet.PERIODS) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            BlockingScreenEvent.NavigateEvent -> {
                onNavigateToLogin()
            }

            is BlockingScreenEvent.OnMessage -> snakeBarHostState.showSnackbar(
                event.message.asString(
                    context
                )
            )
        }
    }

    BlockingScreen(
        state = state,
        snakeBarHostState = snakeBarHostState,
        typeBottomSheet = typeBottomSheet,
        showBottomSheet = showBottomSheet,
        onAction = viewModel::onAction,
        updateShowBottomSheet = {
            showBottomSheet = it
        },
        updateTypeBottomSheet = {
            typeBottomSheet = it
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlockingScreen(
    modifier: Modifier = Modifier,
    state: BlockingScreenState,
    typeBottomSheet: TypeBottomSheet,
    showBottomSheet: Boolean,
    snakeBarHostState: SnackbarHostState,
    onAction: (BlockingScreenAction) -> Unit,
    updateTypeBottomSheet: (TypeBottomSheet) -> Unit,
    updateShowBottomSheet: (Boolean) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    Scaffold(
        snackbarHost = { SnackbarHost(snakeBarHostState) },
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(15.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    if (state.historyBlocks.isEmpty()) {
                        NothingFind()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.historyBlocks) {
                                ItemHistoryBlock(historyBlock = it)
                            }
                        }
                    }
                    /* Text(
                         text = state.selectedDate.toString(),
                         style = MaterialTheme.typography.labelMedium,
                     )
                     DateCalendar(
                         currentDate = state.selectedDate.toJavaLocalDate(),
                         updateCurrentDate = {
                             onAction(BlockingScreenAction.UpdateSelectedDate(it.toKotlinLocalDate()))
                         }
                     )*/
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    updateShowBottomSheet(false)
                    updateTypeBottomSheet(TypeBottomSheet.PERIODS)
                },
            ) {
                when (typeBottomSheet) {
                    TypeBottomSheet.PERIODS -> {
                        ContentHistoryBottomSheet(
                            onClickPeriodWeek = {
                                onAction(BlockingScreenAction.GetHistoryByPeriod(PeriodHistory.LastWeek))
                            },
                            onClickPeriodMonth = {
                                onAction(BlockingScreenAction.GetHistoryByPeriod(PeriodHistory.LastMonth))
                            },
                            onClickPeriodToday = {
                                onAction(BlockingScreenAction.GetHistoryByPeriod(PeriodHistory.Today))
                            },
                            onClickPeriodCustom = {
                                updateTypeBottomSheet(TypeBottomSheet.CALENDAR)
                            }
                        )
                    }

                    TypeBottomSheet.CALENDAR -> {
                        ContentCalendarBottomSheet(
                            onClickCancel = {
                                updateShowBottomSheet(false)
                                updateTypeBottomSheet(TypeBottomSheet.PERIODS)
                            },
                            onClickPeriod = { start, end ->
                                updateShowBottomSheet(false)
                                updateTypeBottomSheet(TypeBottomSheet.PERIODS)
                                onAction(
                                    BlockingScreenAction.GetHistoryByPeriod(
                                        PeriodHistory.SelectedPeriod(
                                            dateFrom = start,
                                            dateTo = end
                                        )
                                    )
                                )
                            }
                        )
                    }
                }

            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        BlockingScreen(
            state = BlockingScreenState(),
            snakeBarHostState = snackBarState,
            typeBottomSheet = TypeBottomSheet.PERIODS,
            showBottomSheet = false,
            onAction = {},
            updateShowBottomSheet = {},
            updateTypeBottomSheet = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview1() {
    val snackBarState = remember { SnackbarHostState() }
    BillingTheme {
        BlockingScreen(
            state = BlockingScreenState(),
            snakeBarHostState = snackBarState,
            typeBottomSheet = TypeBottomSheet.PERIODS,
            showBottomSheet = true,
            onAction = {},
            updateShowBottomSheet = {},
            updateTypeBottomSheet = {}
        )
    }
}

