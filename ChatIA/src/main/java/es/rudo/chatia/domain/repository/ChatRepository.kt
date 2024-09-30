package es.rudo.chatia.domain.repository

import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.entity.Message

interface ChatRepository {
    suspend fun sendMessage(message: String): Message
    suspend fun createChat(customerId: Int)
    suspend fun getConfig(customerId: Int): ConfigChat
}