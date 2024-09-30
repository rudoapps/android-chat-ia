package es.rudo.chatia.domain.usecase.chat

import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.entity.Message

interface ChatUseCase {
    suspend fun sendMessage(message: String): Message
    suspend fun createChat(customerId: Int)
    suspend fun getConfig(customerId: Int): ConfigChat
}