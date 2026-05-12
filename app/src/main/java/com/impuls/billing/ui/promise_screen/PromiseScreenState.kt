package com.impuls.billing.ui.promise_screen

import com.impuls.billing.domain.model.PPSettings
import com.impuls.billing.ui.uikit.UiText

data class PromiseScreenState(
    // val isLoading: Boolean = false,
    val resultUiState: ResultUiState = ResultUiState.Loading,
    val amount: String = "",
    val amountError: AmountTextError = AmountTextError.NO_ERROR
) {
    /*private val success = (resultUiState as ResultUiState.Success)
    val doubleAmount = amount.toDoubleOrNull()
    val isMore = success.ppSettings?.promiseRent == true
    val isError = (amount.isBlank()
            || doubleAmount == null
            || doubleAmount < (success.ppSettings?.promiseMin ?: 0.0)
            || doubleAmount > (success.ppSettings?.promiseMax ?: 1000000.0))
            || (isMore && doubleAmount > success.promisePayment)*/
}

sealed interface ResultUiState {
    data object Loading : ResultUiState
    data class Success(
        val promisePayment: Double = 0.0,
        val ppSettings: PPSettings? = null,
    ) : ResultUiState

    data class Error(val message: UiText? = null) : ResultUiState
}

enum class AmountTextError {
    NO_ERROR,
    NOT_NUMBER,
    MORE_MAX,
    LESS_MIN,
    MORE_RECOMMENDED
}