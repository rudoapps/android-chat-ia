package es.rudo.chatia.data.datasource.chat.remote.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("chat_id")
    val chatId: Int,
    val content: String,
    @SerializedName("assistant_id")
    val assistantId: Int = 1
)
