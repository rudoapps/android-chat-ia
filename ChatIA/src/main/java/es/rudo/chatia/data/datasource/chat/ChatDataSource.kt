package es.rudo.chatia.data.datasource.chat

import es.rudo.chatia.data.datasource.chat.remote.dto.CustomerIdDto
import es.rudo.chatia.data.datasource.chat.remote.dto.MessageDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseConfigChatDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseCreateChatDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseDataMessageDto

interface ChatDataSource {
    suspend fun sendMessage(message: MessageDto): ResponseDataMessageDto
    suspend fun createChat(customerId: CustomerIdDto): ResponseCreateChatDto
    suspend fun getConfig(customerId: CustomerIdDto): ResponseConfigChatDto
}