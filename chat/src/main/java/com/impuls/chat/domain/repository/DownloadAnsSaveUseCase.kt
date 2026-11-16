package com.impuls.chat.domain.repository

import com.impuls.chat.domain.model.DownloadType
import com.impuls.chat.domain.model.DownloadTypeFromUseCase
import com.impuls.chat.domain.utils.ChatDataError
import com.impuls.chat.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DownloadAnsSaveUseCase(
    private val repository: RemoteChatRepository,
    private val fileRepository: FileRepository
) {
    operator fun invoke(
        accessToken: String,
        fileName: String,
        originalName: String,
    ): Flow<DownloadTypeFromUseCase> {
        return flow {
            repository.downloadFile(
                accessToken = accessToken,
                fileName = fileName
            ).collect { collector ->
                when (collector) {
                    is DownloadType.Started -> {
                        emit(DownloadTypeFromUseCase.Started)
                    }

                    is DownloadType.Progress -> {
                        emit(DownloadTypeFromUseCase.Progress(value = collector.value))
                    }

                    is DownloadType.Finished -> {
                        val resultUri = fileRepository.saveFile(
                            byteArray = collector.byteArray,
                            fileName = originalName
                        )
                        when (resultUri) {
                            is ResultWork.Error -> {
                                emit(DownloadTypeFromUseCase.Failed(error = resultUri.error))
                            }

                            is ResultWork.Success -> {
                                emit(DownloadTypeFromUseCase.Finished(uri = resultUri.data))
                            }
                        }
                    }

                    is DownloadType.Failed -> {
                        emit(DownloadTypeFromUseCase.Failed(error = collector.error))
                    }
                }
            }
        }.catch { error ->
            error.printStackTrace()
            emit(DownloadTypeFromUseCase.Failed(error = ChatDataError.Network.UNKNOWN))
        }
    }
}