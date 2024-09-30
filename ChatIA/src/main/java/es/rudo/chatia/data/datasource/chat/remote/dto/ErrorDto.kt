package es.rudo.chatia.data.datasource.chat.remote.dto

data class ErrorDto(
    val field: String?,
    val message: String,
    val input: String?
)
