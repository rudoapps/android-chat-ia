package es.rudo.chatia.data.datasource.chat.remote.dto

data class ResponseCreateChatDto(
    val data: ChatIdDto,
    val error: ErrorDto? = null
)
