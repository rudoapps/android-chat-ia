package es.rudo.chatia.data.repository

import android.content.Context
import es.rudo.chatia.App
import es.rudo.chatia.core.ErrorHandler
import es.rudo.chatia.data.datasource.chat.ChatDataSource
import es.rudo.chatia.data.datasource.chat.remote.dto.ConfigChatDto
import es.rudo.chatia.data.datasource.chat.remote.dto.CustomerIdDto
import es.rudo.chatia.data.datasource.chat.remote.dto.MessageDto
import es.rudo.chatia.data.datasource.chat.remote.dto.ResponseDataMessageDto
import es.rudo.chatia.domain.entity.ConfigChat
import es.rudo.chatia.domain.entity.Message
import es.rudo.chatia.domain.repository.ChatRepository
import es.rudo.chatia.presentation.StoreChatConfig

class ChatRepositoryImpl(
    private val chatDataSource: ChatDataSource,
    private val errorHandler: ErrorHandler
) : ChatRepository {
    companion object {
        private const val CHAT_KEY = "chatKey"
        val sharedPref = App.instance.getSharedPreferences(CHAT_KEY, Context.MODE_PRIVATE)
    }

    override suspend fun sendMessage(message: String): Message {
        try {
            return chatDataSource.sendMessage(message.toData()).toDomain()
        } catch (error: Exception) {
            throw errorHandler.handle(error)
        }
    }

    private fun ResponseDataMessageDto.toDomain() = Message(
        content = this.data.first().message,
        isResponse = true
    )

    private fun String.toData() = MessageDto(
        chatId = sharedPref.getInt(CHAT_KEY, -1),
        content = this
    )

    override suspend fun createChat(customerId: Int) {
        try {
            val chatId = chatDataSource.createChat(customerId.toData()).data.chatId
            sharedPref.edit().putInt(CHAT_KEY, chatId).apply()
        } catch (error: Exception) {
            throw errorHandler.handle(error)
        }
    }

    override suspend fun getConfig(customerId: Int): ConfigChat {
        try {
            val config = chatDataSource.getConfig(customerId.toData()).data.toDomain()
            StoreChatConfig(App.instance).saveConfig(config.colorOne, config.colorTwo)
            return config
        } catch (error: Exception) {
            throw errorHandler.handle(error)
        }
    }

    private fun Int.toData() = CustomerIdDto(
        customerId = this
    )

    private fun ConfigChatDto.toDomain() = ConfigChat(
        colorOne = this.colorOne,
        colorTwo = this.colorTwo,
        firstMessage = this.firstMessage
    )
}