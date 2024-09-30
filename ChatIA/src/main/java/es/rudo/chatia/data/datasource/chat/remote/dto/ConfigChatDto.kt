package es.rudo.chatia.data.datasource.chat.remote.dto

import com.google.gson.annotations.SerializedName

data class ConfigChatDto(
    val name: String,
    @SerializedName("color1")
    val colorOne: String,
    @SerializedName("color2")
    val colorTwo: String,
    @SerializedName("first_message")
    val firstMessage: String
)
