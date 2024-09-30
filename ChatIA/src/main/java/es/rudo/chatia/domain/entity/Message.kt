package es.rudo.chatia.domain.entity

data class Message(
    val isResponse: Boolean,
    val content: String
)