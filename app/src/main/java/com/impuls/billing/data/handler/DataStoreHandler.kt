package com.impuls.billing.data.handler


import com.impuls.billing.domain.utils.DataError
import com.impuls.billing.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

suspend fun <D> handleWrite(data: suspend () -> D): ResultWork<D, DataError.DataStore> {
    return try {
        ResultWork.Success(data())

    }  catch (e: Exception) {
        e.printStackTrace()
        ResultWork.Error(DataError.DataStore.ERROR_WRITE_DATA)
    }
}

fun <D> handleReadFlow(data: () -> Flow <D>): Flow<ResultWork<D, DataError.DataStore>> {
    return flow<ResultWork<D, DataError.DataStore>> {
        data.invoke().collect { collector ->
            emit(
                ResultWork.Success(collector)
            )
        }
    }.catch {
        emit(
            ResultWork.Error(DataError.DataStore.ERROR_READ_DATA)
        )
    }
}

suspend fun <D> handleRead(data: suspend () -> D): ResultWork<D, DataError.DataStore> {
    return try {
        ResultWork.Success(data())

    }  catch (e: Exception) {
        e.printStackTrace()
        ResultWork.Error(DataError.DataStore.ERROR_READ_DATA)
    }
}