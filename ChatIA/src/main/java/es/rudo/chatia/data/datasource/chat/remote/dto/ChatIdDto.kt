package es.rudo.chatia.data.datasource.chat.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatIdDto(
    @SerializedName("chat_id")
    val chatId: Int
)
