package com.krobys.skytest.manager

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class AppMessage(
    val message: String,
    val timestamp: Long,
    val messageType: AppMessageType,
    val messageDuration: Long = 3000L
) {
    enum class AppMessageType {
        ERROR, INFO
    }
}

object MessageManager {

    private val _appMessages = MutableSharedFlow<AppMessage>()
    val appMessages: SharedFlow<AppMessage> = _appMessages

    suspend fun postError(errorMessage: String) {
        _appMessages.emit(
            AppMessage(
                message = errorMessage,
                timestamp = System.currentTimeMillis(),
                messageType = AppMessage.AppMessageType.ERROR
            )
        )
    }

    suspend fun postInfoMessage(message: String) {
        _appMessages.emit(
            AppMessage(
                message = message,
                timestamp = System.currentTimeMillis(),
                messageType = AppMessage.AppMessageType.INFO
            )
        )
    }

    suspend fun postError(exception: Throwable) {
        _appMessages.emit(
            AppMessage(
                message = exception.message ?: "Something went wrong",
                timestamp = System.currentTimeMillis(),
                messageType = AppMessage.AppMessageType.ERROR
            )
        )
    }
}
