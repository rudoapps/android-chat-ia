package es.rudo.chatia.domain.usecase.chat

import es.rudo.chatia.core.ErrorHandler
import es.rudo.chatia.domain.entity.AppError
import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.entity.Message
import es.rudo.chatia.domain.repository.ChatRepository

sealed class ChatError : Exception() {
    class NoInternet : ChatError()
    class UnknownError : ChatError()
    class EmptyResponse : ChatError()
    class NetworkError : ChatError()
}

class ChatUseCaseImpl(
    private val chatRepository: ChatRepository
): ChatUseCase, ErrorHandler {
    override suspend fun sendMessage(message: String): Message {
       return try {
            chatRepository.sendMessage(message)
        } catch (error: Exception){
            throw handle(error)
        }
    }
    override suspend fun createChat(customerId: Int) {
        try {
            chatRepository.createChat(customerId)
        } catch (error: Exception){
            throw handle(error)
        }
    }

    override suspend fun getConfig(customerId: Int): ConfigChat {
        return try {
            chatRepository.getConfig(customerId)
        } catch (error: Exception){
            throw handle(error)
        }
    }

    override fun handle(error: Exception): Exception {
        return when (error) {
            is AppError.NetworkError, AppError.Timeout -> ChatError.NetworkError()
            is AppError.ResponseWithEmptyBody -> ChatError.EmptyResponse()
            is AppError.NoInternet-> ChatError.NoInternet()
            else -> {
                ChatError.UnknownError()
            }
        }
    }
}