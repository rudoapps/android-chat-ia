package es.rudo.chatia.data.datasource.chat.remote.dto

import com.google.gson.annotations.SerializedName

class ResponseMessageDto(
    @SerializedName("value")
    val message: String,
    val role : String
)