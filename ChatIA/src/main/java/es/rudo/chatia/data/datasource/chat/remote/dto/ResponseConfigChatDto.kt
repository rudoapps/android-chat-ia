package es.rudo.chatia.data.datasource.chat.remote.dto

data class ResponseConfigChatDto(
    val data: ConfigChatDto,
    val error: ErrorDto? = null
)