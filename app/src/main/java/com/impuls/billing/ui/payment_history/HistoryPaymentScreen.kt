package com.impuls.billing.ui.payment_history

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.impuls.billing.R
import com.impuls.billing.domain.model.PeriodHistory
import com.impuls.billing.ui.theme.BillingTheme
import com.impuls.billing.ui.uikit.NothingFind
import com.impuls.billing.ui.uikit.ObserveAsEvents
import com.impuls.billing.ui.uikit.asPeriodText
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryPaymentScreenRoot(
    viewModel: HistoryPaymentScreenViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var showBottomSheet by remember { mutableStateOf(false) }
    var typeBottomSheet by remember { mutableStateOf(TypeBottomSheet.PERIODS) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            HistoryPaymentScreenEvent.NavigateEvent -> onNavigateToLogin()
            is HistoryPaymentScreenEvent.OnMessage -> snackbarHostState.showSnackbar(
                event.message.asString(
                    context
                )
            )
        }
    }

    HistoryPaymentScreen(
        state = state,
        showBottomSheet = showBottomSheet,
        typeBottomSheet = typeBottomSheet,
        snackbarHostState = snackbarHostState,
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
private fun HistoryPaymentScreen(
    modifier: Modifier = Modifier,
    state: HistoryPaymentScreenState,
    typeBottomSheet: TypeBottomSheet,
    snackbarHostState: SnackbarHostState,
    showBottomSheet: Boolean,
    onAction: (HistoryPaymentScreenAction) -> Unit,
    updateShowBottomSheet: (Boolean) -> Unit,
    updateTypeBottomSheet: (TypeBottomSheet) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.history_operations),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.clickable {
                            updateShowBottomSheet(true)
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = state.periodHistory.asPeriodText().asString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.arrow_drop_down_24dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TypeScreen.entries.forEach {
                            TypeScreenTab(
                                modifier = Modifier.weight(1f),
                                icon = ImageVector.vectorResource(it.iconRes),
                                stringValue = when (it) {
                                    TypeScreen.WRITEOFF -> {
                                        "-${"%.2f".format(state.debitsSum)} ${stringResource(R.string.rub_symbol)}"
                                    }

                                    TypeScreen.PAYMENT -> {
                                        "${state.paymentsSum} ${stringResource(R.string.rub_symbol)}"
                                    }
                                },
                                title = stringResource(it.titleRes),
                                isChecked = state.typeScreen == it,
                                onClick = {
                                    onAction(HistoryPaymentScreenAction.SwitchTypeScreen(it))
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    when (state.typeScreen) {
                        TypeScreen.WRITEOFF -> {
                            if (state.historyDebits.isEmpty()) {
                                NothingFind()
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(state.historyDebits) {
                                        HistoryDebitCard(
                                            history = it
                                        )
                                    }
                                }
                            }
                        }

                        TypeScreen.PAYMENT -> {
                            if (state.historyPayments.isEmpty()) {
                                NothingFind()
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(state.historyPayments) {
                                        ItemHistoryPayment(
                                            historyPayment = it
                                        )
                                    }
                                }
                            }
                        }
                    }
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
                                onAction(HistoryPaymentScreenAction.getHistoryByPeriod(PeriodHistory.LastWeek))
                            },
                            onClickPeriodMonth = {
                                onAction(HistoryPaymentScreenAction.getHistoryByPeriod(PeriodHistory.LastMonth))
                            },
                            onClickPeriodToday = {
                                onAction(HistoryPaymentScreenAction.getHistoryByPeriod(PeriodHistory.Today))
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
                                    HistoryPaymentScreenAction.getHistoryByPeriod(
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
    val snackbarHostState = remember { SnackbarHostState() }
    BillingTheme {
        HistoryPaymentScreen(
            state = HistoryPaymentScreenState(
                agrmId = 0
            ),
            typeBottomSheet = TypeBottomSheet.PERIODS,
            showBottomSheet = false,
            snackbarHostState = snackbarHostState,
            onAction = {},
            updateShowBottomSheet = {},
            updateTypeBottomSheet = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview2() {
    val snackbarHostState = remember { SnackbarHostState() }
    BillingTheme {
        HistoryPaymentScreen(
            state = HistoryPaymentScreenState(
                agrmId = 0
            ),
            typeBottomSheet = TypeBottomSheet.PERIODS,
            showBottomSheet = true,
            snackbarHostState = snackbarHostState,
            onAction = {},
            updateShowBottomSheet = {},
            updateTypeBottomSheet = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun Preview3() {
    val snackbarHostState = remember { SnackbarHostState() }
    BillingTheme {
        HistoryPaymentScreen(
            state = HistoryPaymentScreenState(
                agrmId = 0
            ),
            typeBottomSheet = TypeBottomSheet.CALENDAR,
            showBottomSheet = true,
            snackbarHostState = snackbarHostState,
            onAction = {},
            updateShowBottomSheet = {},
            updateTypeBottomSheet = {}
        )
    }
}
