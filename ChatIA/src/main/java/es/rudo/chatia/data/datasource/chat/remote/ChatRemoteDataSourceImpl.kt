package es.rudo.chatia.data.datasource.chat.remote

import es.rudo.chatia.data.datasource.chat.ChatDataSource
import es.rudo.chatia.data.datasource.chat.remote.api.ChatApi
import es.rudo.chatia.data.datasource.chat.remote.dto.CustomerIdDto
import es.rudo.chatia.data.datasource.chat.remote.dto.MessageDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseConfigChatDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseMessageDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseCreateChatDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseDataMessageDto
import es.rudo.chatia.di.NetworkMoshiMoshi

class ChatRemoteDataSourceImpl (
    private val networkMoshiMoshi: NetworkMoshiMoshi
): ChatDataSource {
    private val chatApi = networkMoshiMoshi.instance.create(ChatApi::class.java)

    override suspend fun sendMessage(message: MessageDto) =
         networkMoshiMoshi.instance.load {
            chatApi.sendMessage(message)
        }

    override suspend fun createChat(customerId: CustomerIdDto) =
        networkMoshiMoshi.instance.load {
            chatApi.createChat(customerId)
        }

    override suspend fun getConfig(customerId: CustomerIdDto) =
         networkMoshiMoshi.instance.load {
            chatApi.getConfig(customerId)
        }
}